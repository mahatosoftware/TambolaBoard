package in.mahato.tambola;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private DBHelper dbh;
    private SQLiteDatabase db;
    private RecyclerView rv;
    private BoardAdapter adapter;
    private Button btnNext, btnReset, btnExit;
    private TextView tvLast;
    private List<Integer> pool = new ArrayList<>();
    private Set<Integer> calledSet = new HashSet<>();
    private Integer lastCalled = null;
    private long gameId = -1;
    private TextToSpeech tts;
    private Boolean isTexttoSpeechInitialized = false;
    private boolean funnyMode = true; // enable funny calling by default
    private static final String PREFS = "tambola_prefs";
    private static final String PREF_LAST_GAME = "last_game_id";

    private boolean orientationChanged = false;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbh = new DBHelper(this);
        db = dbh.getWritableDatabase();

        btnNext = findViewById(R.id.btnCallNext);
        btnReset = findViewById(R.id.btnReset);
        btnExit = findViewById(R.id.btnExit);
        tvLast = findViewById(R.id.tvLastCalled);
        tvLast.setTextColor(0xFFFFFFFF);
        rv = findViewById(R.id.rvBoard);

        TextView textCopyright = findViewById(R.id.textCopyright);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String copyright = "© " + year + " Debasish Mahato.";
        textCopyright.setText(copyright);

        adapter = new BoardAdapter(this);
        rv.setAdapter(adapter);
        GridLayoutManager gm = new GridLayoutManager(this, 10);
        rv.setLayoutManager(gm);

        tts = new TextToSpeech(this, this);

        boolean newGame = getIntent().getBooleanExtra("NEW_GAME", true);
        if (savedInstanceState != null) {
            orientationChanged = savedInstanceState.getBoolean("OrientationChanged");
        }
        if(orientationChanged){
            newGame = false;
        }
        if (newGame) startNewGame();
        else continueLastGamePrompt();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextNumber();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReset();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmExit();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("OrientationChanged", true);
        orientationChanged = true;

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        orientationChanged = savedInstanceState.getBoolean("OrientationChanged");



    }


    private void startNewGame() {
        // create a new game id and persist it
        gameId = System.currentTimeMillis();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        prefs.edit().putLong(PREF_LAST_GAME, gameId).apply();

        pool.clear();
        for (int i = 1; i <= 90; i++) pool.add(i);
        Collections.shuffle(pool);
        calledSet.clear();
        lastCalled = null;
        adapter.setCalled(calledSet, lastCalled);
    }

    private void continueLastGamePrompt() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        long last = prefs.getLong(PREF_LAST_GAME, -1);
        if (last == -1) {
            // nothing to continue; start new
            startNewGame();
            return;
        }
        gameId = last;
        loadGame(gameId);
    }

    private void loadGame(long gid) {
        calledSet.clear();
        lastCalled = null;
        Cursor c = db.query(GameContract.TABLE_CALLED, null, GameContract.Columns.GAME_ID + "=?",
                new String[]{String.valueOf(gid)}, null, null, GameContract.Columns.ORDER_INDEX + " ASC");
        List<Integer> order = new ArrayList<>();
        while (c.moveToNext()) {
            int num = c.getInt(c.getColumnIndexOrThrow(GameContract.Columns.NUMBER));
            order.add(num);
            calledSet.add(num);
        }
        c.close();
        pool.clear();
        for (int i = 1; i <= 90; i++) if (!calledSet.contains(i)) pool.add(i);
        Collections.shuffle(pool);
        lastCalled = order.size() > 0 ? order.get(order.size() - 1) : null;
        String lastNumCalledText = getString(R.string.txt_last_number_called);
        if (lastCalled != null) tvLast.setText(lastNumCalledText + lastCalled);
        adapter.setCalled(calledSet, lastCalled);
    }

    private void callNextNumber() {
        if (pool.isEmpty()) {
            speak("All numbers have been called");
            return;
        }
        int next = pool.remove(0);
        calledSet.add(next);
        lastCalled = next;
        String lastNumCalledText = getString(R.string.txt_last_number_called);
        tvLast.setText(lastNumCalledText + next);
        saveCalledNumber(next);
        adapter.setCalled(calledSet, lastCalled);
        speakNumber(next);

    }

    private void saveCalledNumber(int number) {
        ContentValues v = new ContentValues();
        v.put(GameContract.Columns.GAME_ID, gameId);
        v.put(GameContract.Columns.NUMBER, number);
        v.put(GameContract.Columns.ORDER_INDEX, calledSet.size());
        v.put(GameContract.Columns.TS, System.currentTimeMillis());
        db.insert(GameContract.TABLE_CALLED, null, v);
    }

    private void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Game")
                .setMessage("Are you sure you want to reset the current game? This will clear called numbers for this session.")
                .setPositiveButton("Yes", (d, w) -> {
                    // delete rows for this game id and start new
                    db.delete(GameContract.TABLE_CALLED, GameContract.Columns.GAME_ID + "=?", new String[]{String.valueOf(gameId)});
                    startNewGame();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit the current game?")
                .setPositiveButton("Yes", (d, w) -> {
                    // Exit Game
                    finish();
                    System.exit(0);

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void speakNumber(int num) {
        if (tts == null) return;
        String toSpeak = getFunnyPhrase(num);
        // tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "NUM_" + num);
        speak(toSpeak);
    }

    private void speak(String text) {
        if (tts != null) {
            tts.stop();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "GENERIC");
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }


    public static String getFunnyPhrase(int number) {
        String text;
        switch (number) {
            case 1:
                text = "At the Begining.";
                break;
            case 2:
                text = "Me and you.";
                break;
            case 3:
                text = "Happy family.";
                break;
            case 4:
                text = "Two Plus Two.";
                break;
            case 5:
                text = "Punjab mail.";
                break;
            case 6:
                text = "Bottom heavy.";
                break;
            case 7:
                text = "Lucky number.";
                break;
            case 8:
                text = "Big fat lady.";
                break;
            case 9:
                text = "Doctor's time.";
                break;
            case 10:
                text = "A big fat hen.";
                break;
            case 11:
                text = "One and one.";
                break;
            case 12:
                text = "One dozen.";
                break;
            case 13:
                text = "Unlucky for some.";
                break;
            case 14:
                text = "Valentine's Day.";
                break;
            case 15:
                text = "The age when attitude starts.";
                break;
            case 16:
                text = "Sweet sixteen.";
                break;
            case 17:
                text = "Not so sweet.";
                break;
            case 18:
                text = "Voting age.";
                break;
            case 19:
                text = "Last of the teens.";
                break;
            case 20:
                text = "One score.";
                break;
            case 21:
                text = "Women's age never crosses.";
                break;
            case 22:
                text = "Two little ducks.";
                break;
            case 23:
                text = "You and me.";
                break;
            case 24:
                text = "Two dozen.";
                break;
            case 25:
                text = "Silver Jubilee Number.";
                break;
            case 26:
                text = "Republic Day";
                break;
            case 27:
                text = "Gateway to heaven";
                break;
            case 28:
                text = "Not so late at.";
                break;
            case 29:
                text = "Rise and Shine at.";
                break;
            case 30:
                text = "Women get flirty at.";
                break;
            case 31:
                text = "Time for fun.";
                break;
            case 32:
                text = "Buckle my shoe.";
                break;
            case 33:
                text = "All the 3s.";
                break;
            case 34:
                text = "Ask for more.";
                break;
            case 35:
                text = "Three and Five.";
                break;
            case 36:
                text = "Popular size.";
                break;
            case 37:
                text = "Mixed luck.";
                break;
            case 38:
                text = "Oversize.";
                break;
            case 39:
                text = "Watch your waistline.";
                break;
            case 40:
                text = "Men get Naughty at.";
                break;
            case 41:
                text = "Four and one.";
                break;
            case 42:
                text = "Quit India Movement.";
                break;
            case 43:
                text = "Pain in the knee.";
                break;
            case 44:
                text = "All the 4's.";
                break;
            case 45:
                text = "Halfway there.";
                break;
            case 46:
                text = "Four and six.";
                break;
            case 47:
                text = "Year of Independence.";
                break;
            case 48:
                text = "Four dozen.";
                break;
            case 49:
                text = "Four and Nine.";
                break;
            case 50:
                text = "Half a century";
                break;
            case 51:
                text = "Five and one.";
                break;
            case 52:
                text = "Weeks in a year.";
                break;
            case 53:
                text = "Five and three.";
                break;
            case 54:
                text = "Time for Mooor.";
                break;
            case 55:
                text = "All the fives.";
                break;
            case 56:
                text = "Pick up sticks.";
                break;
            case 57:
                text = "Mutiny Year.";
                break;
            case 58:
                text = "Time to retire.";
                break;
            case 59:
                text = "Five and Nine.";
                break;
            case 60:
                text = "Five dozen.";
                break;
            case 61:
                text = "Bakers bun.";
                break;
            case 62:
                text = "Turn the screw.";
                break;
            case 63:
                text = "Tickle me.";
                break;
            case 64:
                text = "Six and Four.";
                break;
            case 65:
                text = "Old age pension.";
                break;
            case 66:
                text = "Chhakke pe chhakka.";
                break;
            case 67:
                text = "Made in heaven.";
                break;
            case 68:
                text = "Check your weight.";
                break;
            case 69:
                text = "Favourate of mine.";
                break;
            case 70:
                text = "Lucky blind.";
                break;
            case 71:
                text = "Bang on the drum.";
                break;
            case 72:
                text = "Lucky two.";
                break;
            case 73:
                text = "Under the tree.";
                break;
            case 74:
                text = "Still want more.";
                break;
            case 75:
                text = "Diamond Jublee.";
                break;
            case 76:
                text = "Lucky six.";
                break;
            case 77:
                text = "Two hockey sticks.";
                break;
            case 78:
                text = "Heaven's gate.";
                break;
            case 79:
                text = "One more time.";
                break;
            case 80:
                text = "Eight and Blank.";
                break;
            case 81:
                text = "Corner shot.";
                break;
            case 82:
                text = "Fat lady with a duck.";
                break;
            case 83:
                text = "India wins Cricket World Cup at.";
                break;
            case 84:
                text = "Seven Dozen.";
                break;
            case 85:
                text = "Staying alive.";
                break;
            case 86:
                text = "Between the sticks.";
                break;
            case 87:
                text = "Grandpa age.";
                break;
            case 88:
                text = "Two fat ladies.";
                break;
            case 89:
                text = "All but one.";
                break;
            case 90:
                text = "Top of the house.";
                break;
            default:
                text = "Number out of range";
                break;
        }
        return text + "Number " + numberToWords(number);
    }

    public static String numberToWords(int number) {
        String[] units = {
                "", "One", "Two", "Three", "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
                "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
        };

        String[] tens = {
                "", "", "Twenty", "Thirty", "Forty", "Fifty",
                "Sixty", "Seventy", "Eighty", "Ninety"
        };

        if (number <= 0 || number > 90) {
            return "Out of range";
        }

        if (number < 20) {
            return units[number];
        }

        int tensPlace = number / 10;
        int onesPlace = number % 10;

        if (onesPlace == 0) {
            return tens[tensPlace];
        } else {
            return tens[tensPlace] + " " + units[onesPlace];
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale loc = new Locale("hi", "IN");
            int res = tts.setLanguage(loc);
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setLanguage(Locale.US);
            }
            tts.setPitch(1.0f);
            tts.setSpeechRate(0.9f);
            List<TextToSpeech.EngineInfo> engines = tts.getEngines();
            if (!engines.isEmpty()) {
                isTexttoSpeechInitialized = true;
            } else {
                isTexttoSpeechInitialized = false;
                Toast.makeText(GameActivity.this, "Failed to load Text to Speech Engine.App will Work without Voice", Toast.LENGTH_LONG).show();
            }

            if (isTexttoSpeechInitialized) {
                speak("The Party starts here. Welcome to Tambola Board");
            } else {
                Toast.makeText(GameActivity.this, "Failed to load Text to Speech Engine.Game will Work without Voice", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(GameActivity.this, "Failed to load Text to Speech Engine.Game will continue without Voice", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (db != null) db.close();
    }

    // handle remote: D-pad center / enter / media play to call next
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            callNextNumber();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
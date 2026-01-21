import React from 'react';
import clsx from 'clsx';
import { useGame } from '../context/GameContext';
import './Board.css';

const Board = () => {
    const { calledNumbers, lastNumber } = useGame();

    const numbers = Array.from({ length: 90 }, (_, i) => i + 1);

    return (
        <div className="board-container">
            {numbers.map((number) => {
                const isCalled = calledNumbers.includes(number);
                const isLast = lastNumber === number;

                return (
                    <div
                        key={number}
                        className={clsx('board-cell', {
                            called: isCalled,
                            last: isLast,
                        })}
                    >
                        {number}
                    </div>
                );
            })}
        </div>
    );
};

export default Board;

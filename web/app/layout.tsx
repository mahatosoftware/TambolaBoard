import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Tambola Board",
  description: "A lively host board for Tambola games"
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="en"><body>{children}</body></html>;
}

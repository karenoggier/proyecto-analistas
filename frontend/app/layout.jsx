import React from "react"
import { Metadata } from "next"
import { Poppins } from "next/font/google"
import { AppDialogProvider } from "../components/ui/app-dialog"
import "./globals.css"

const poppins = Poppins({
  subsets: ["latin"],
  weight: ["400", "500", "600", "700"],
  variable: "--font-poppins",
})

export const metadata = {
  title: "PediloYa - Tu comida favorita a tu alcance",
  description: "Pedí tu comida favorita y recibila en tu casa. Entregas rápidas en toda Argentina.",
    generator: 'v0.app'
}

export default function RootLayout({children}) {
  return (
    <html lang="es">
      <body className={poppins.className}>
        <AppDialogProvider>{children}</AppDialogProvider>
      </body>
    </html>
  )
}

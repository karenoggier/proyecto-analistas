"use client"

import { createContext, useCallback, useContext, useMemo, useState } from "react"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./alert-dialog"
import styles from "./app-dialog.module.css"

const AppDialogContext = createContext({
  showAlert: async () => {},
  showConfirm: async () => false,
})

export function AppDialogProvider({ children }) {
  const [dialog, setDialog] = useState({
    open: false,
    type: "alert",
    title: "",
    description: "",
    confirmText: "Aceptar",
    cancelText: "Cancelar",
    resolve: null,
  })

  const closeDialog = useCallback((result) => {
    setDialog((prev) => {
      if (prev.resolve) prev.resolve(result)
      return { ...prev, open: false, resolve: null }
    })
  }, [])

  const showAlert = useCallback((options) => {
    const config = typeof options === "string" ? { description: options } : options || {}

    return new Promise((resolve) => {
      setDialog({
        open: true,
        type: "alert",
        title: config.title || "Aviso",
        description: config.description || "",
        confirmText: config.confirmText || "Entendido",
        cancelText: "",
        resolve,
      })
    })
  }, [])

  const showConfirm = useCallback((options) => {
    const config = typeof options === "string" ? { description: options } : options || {}

    return new Promise((resolve) => {
      setDialog({
        open: true,
        type: "confirm",
        title: config.title || "Confirmar acción",
        description: config.description || "",
        confirmText: config.confirmText || "Confirmar",
        cancelText: config.cancelText || "Cancelar",
        resolve,
      })
    })
  }, [])

  const value = useMemo(() => ({ showAlert, showConfirm }), [showAlert, showConfirm])

  return (
    <AppDialogContext.Provider value={value}>
      {children}

      <AlertDialog open={dialog.open} onOpenChange={(open) => !open && closeDialog(false)}>
        <AlertDialogContent className={styles.content}>
          <AlertDialogHeader className={styles.header}>
            <AlertDialogTitle className={styles.title}>{dialog.title}</AlertDialogTitle>
            <AlertDialogDescription className={styles.description}>{dialog.description}</AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter className={styles.footer}>
            {dialog.type === "confirm" && (
              <AlertDialogCancel className={styles.cancelButton} onClick={() => closeDialog(false)}>
                {dialog.cancelText}
              </AlertDialogCancel>
            )}
            <AlertDialogAction className={styles.actionButton} onClick={() => closeDialog(true)}>
              {dialog.confirmText}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </AppDialogContext.Provider>
  )
}

export function useAppDialog() {
  return useContext(AppDialogContext)
}

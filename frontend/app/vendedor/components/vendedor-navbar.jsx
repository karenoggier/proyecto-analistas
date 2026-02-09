"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import Image from "next/image"
import styles from "./vendedor-navbar.module.css" 

export default function VendedorNavbar({ profile }) {
  const router = useRouter()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)

  const handleNavigate = (path) => {
    router.push(path)
    setShowUserMenu(false)
  }

  const handleLogout = () => {
    localStorage.clear()
    window.location.href = "/login"
  }

  const getDireccionTexto = () => {
    if (!profile || !profile.direccion) return "Mi dirección"
    const { calle, numero, localidad } = profile.direccion
    return `${calle || ''} ${numero || ''}, ${localidad || ''}`
  }

  return (
    <>
      {/* ========== NAVBAR ========== */}
      <nav className={styles.navbar}>
        <div className={styles.navbarInner}>
          <div className={styles.navbarLeft}>
            <Link href="/vendedor" className={styles.logo}>
              <Image src="/logo.png" alt="PediloYa Logo" width={50} height={60} className={styles.logo} priority />
              <span className={styles.logoText}>PediloYa</span>
            </Link>
            <div className={styles.location}>
              <Image src="/pin-de-ubicacion.png" alt="Pin de ubicación" width={30} height={40} />
              <div className={styles.locationText}>
                <span className={styles.locationLabel}>Ubicación</span>
                <span className={styles.locationValue}>
                  {profile?.direccion 
                  ? `${profile.direccion.calle} ${profile.direccion.numero}, ${profile.direccion.localidad}`
                  : "Mi dirección"}
                </span>
              </div>
            </div>
          </div>

          <div className={styles.navbarRight}>
            <div className={styles.navbarIconWrapper}>
              <button
                className={styles.navbarIcon}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowNotifications(!showNotifications)
                  setShowUserMenu(false)
                }}
              >
                <Image src="/campana-de-notificacion.png" alt="Notificaciones" width={28} height={38} />
              </button>
              {showNotifications && (
                <div className={styles.notificationPopover}>
                  <div className={styles.notificationHeader}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="#9ca3af">
                      <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z" />
                    </svg>
                    <span>Notificaciones</span>
                  </div>
                  <div className={styles.notificationContent}>
                    <p>No tenés notificaciones</p>
                  </div>
                </div>
              )}
            </div>

            <div className={styles.navbarIconWrapper}>
              <button
                className={styles.userButton}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowUserMenu(!showUserMenu)
                  setShowNotifications(false)
                }}
              >
                {profile?.logo ? (
                <img 
                    src={profile.logo} 
                    alt="Logo negocio" 
                    className={styles.userAvatar} // Clase nueva para hacerlo redondo
                  />
                ) : (
                <Image src="/perfil.png" alt="Foto de perfil" width={35} height={45} />
                )}
                <span className={styles.userButtonText}>{profile?.nombreNegocio || "Mi Cuenta"}</span>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="#374151">
                  <path d="M7 10l5 5 5-5z" />
                </svg>
              </button>
              {showUserMenu && (
                <div className={styles.userPopover}>
                  <div className={styles.userPopoverHeader}>
                    <span>{profile?.nombreNegocio || "Usuario"}</span>
                  </div>
                  <div className={styles.userPopoverMenu}>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
                      </svg>
                      <span>Inicio</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/perfil")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z" />
                      </svg>
                      <span>Mi perfil</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/productos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M20 4H4v2h16V4zm1 10v-2l-1-5H4l-1 5v2h1v6h10v-6h4v6h2v-6h1zm-9 4H6v-4h6v4z" />
                      </svg>
                      <span>Mis productos</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor/pedidos")}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z" />
                      </svg>
                      <span>Pedidos</span>
                    </button>
                    <button className={styles.userPopoverItem}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58.55 0 1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41 0-.55-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
                      </svg>
                      <span>Cupones</span>
                    </button>
                    <button className={styles.userPopoverItem} onClick={handleLogout}>
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
                      </svg>
                      <span>Salir</span>
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
    </>
  )
}
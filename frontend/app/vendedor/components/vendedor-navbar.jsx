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

  /*const handleNavigate = (path) => {
    router.push(path)
    setShowUserMenu(false)
  }*/

  const handleLogout = () => {
    sessionStorage.clear()
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
                className={styles.iconBtn}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowNotifications(!showNotifications)
                  setShowUserMenu(false)
                }}
              >
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                  <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
                  <path d="M13.73 21a2 2 0 01-3.46 0" />
                </svg>
                {/*<Image src="/campana-de-notificacion.png" alt="Notificaciones" width={28} height={38} /> */}
              </button>
              {showNotifications && (
                <div className={styles.popover}>
                  <div className={styles.popoverHeader}>
                    <div className={styles.popoverHeaderIcon}>
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                        <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
                        <path d="M13.73 21a2 2 0 01-3.46 0" />
                      </svg>
                      <span>Notificaciones</span>
                    </div>
                  </div>
                  <div className={styles.emptyNotif}>
                    <strong>No tenes notificaciones</strong>
                    <p>¡Aprovecha para descubrir productos increibles!</p>
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
                    className={styles.userAvatar} 
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
                <div className={styles.popover}>
                  <div className={styles.userMenuHeader}>
                    <div className={styles.avatar}>
                      {profile?.logo ? (
                        <img 
                            src={profile.logo} 
                            alt="Logo negocio" 
                            className={styles.userAvatar} 
                          />
                        ) : (
                        <Image src="/perfil.png" alt="Foto de perfil" width={35} height={45} />
                        )}
                    </div>
                    <span>{profile?.nombreNegocio || "Usuario"}</span>
                  </div>
                  <ul className={styles.userMenuList}>
                    <li>
                      <Link href="/vendedor" onClick={() => setShowUserMenu(false)}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" /><polyline points="9 22 9 12 15 12 15 22" /></svg>
                        Inicio
                      </Link>
                    </li>
                    <li>
                      <Link href="/vendedor/perfil" onClick={() => setShowUserMenu(false)}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" /><circle cx="12" cy="7" r="4" /></svg>
                        Mi perfil
                       </Link>
                    </li>
                    <li>
                      <Link href="/vendedor/productos" onClick={() => setShowUserMenu(false)}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <path d="M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z" />
                          <path d="m3.3 7 8.7 5 8.7-5" />
                          <path d="M12 22V12" />
                        </svg>
                        Mis productos
                       </Link>
                    </li>
                    <li>
                      <Link href="/vendedor/pedidos" onClick={() => setShowUserMenu(false)}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <path d="M16 2H10a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V6z" />
                          <path d="M12 18h4" /><path d="M12 14h4" /><path d="M12 10h4" /><path d="M16 2v4h4" />
                        </svg>
                        Pedidos
                       </Link>
                    </li>
                    <li>
                      <div className={styles.disabledLinkContent}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <path d="M15 5V7" /><path d="M15 11V13" /><path d="M15 17V19" />
                          <path d="M5 5h14a2 2 0 0 1 2 2v3a2 2 0 0 0 0 4v3a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-3a2 2 0 0 0 0-4V7a2 2 0 0 1 2-2z" />
                        </svg>
                        Cupones
                        <span className={styles.comingSoon}>Próximamente</span>
                      </div>
                    </li>
                     <li>
                      <Link href="/" className={styles.logoutLink} onClick={() => setUserOpen(false)}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" /><polyline points="16 17 21 12 16 7" /><line x1="21" y1="12" x2="9" y2="12" /></svg>
                        Salir
                      </Link>
                    </li>
                    
                    {/*<button className={styles.userPopoverItem} onClick={() => handleNavigate("/vendedor")}>
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
                    </button>*/}

                  </ul>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
    </>
  )
}
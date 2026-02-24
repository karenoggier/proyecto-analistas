"use client"

import { useState, useEffect, useRef } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import Image from "next/image"
import styles from "./vendedor-navbar.module.css" 
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import toast, { Toaster } from 'react-hot-toast';

export default function VendedorNavbar({ profile }) {
  const router = useRouter()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  
  // --- Lógica de Notificaciones ---
  const [notificaciones, setNotificaciones] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const notifRef = useRef(null);
  const notifOpenRef = useRef(false);

  // Sincronizar ref con estado para el callback del socket
  useEffect(() => {
    notifOpenRef.current = showNotifications;
  }, [showNotifications]);

  // 1. Cargar notificaciones iniciales
  useEffect(() => {
    const fetchNotif = async () => {
      const token = sessionStorage.getItem("token");
      try {
        const res = await fetch('/pedidoMs/notificaciones', {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (res.ok) {
          const data = await res.json();
          const normalized = data.map((n) => ({
            ...n,
            leida: Boolean(n.leida),
            destacada: Boolean(n.destacada),
          }));
          setNotificaciones(normalized);
        }
      } catch (error) {
        console.error("Error cargando notificaciones", error);
      }
    };
    fetchNotif();
  }, []);

  // 2. Calcular contador
  useEffect(() => {
    setUnreadCount(notificaciones.filter(n => !n.leida).length);
  }, [notificaciones]);

  // 3. Conexión WebSocket
  useEffect(() => {
    const wsUrl = process.env.NEXT_PUBLIC_WS_URL || 'http://localhost:8082/pedidoMs/ws-notifications';
    const socket = new SockJS(wsUrl);
    const token = sessionStorage.getItem("token");
    
    const client = new Client({
      webSocketFactory: () => socket,
      connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
      debug: (str) => console.log(str),
      onConnect: () => {
        console.log("¡Vendedor conectado al WS!");
        client.subscribe('/user/queue/updates', (msg) => {
          const nuevaNotif = JSON.parse(msg.body);
          const isOpen = notifOpenRef.current;
          
          const normalizada = {
            ...nuevaNotif,
            leida: isOpen ? true : Boolean(nuevaNotif.leida),
            destacada: isOpen ? true : Boolean(nuevaNotif.destacada),
          };

          // Toast flotante
          toast.custom((t) => (
            <div className={`${styles.toast} ${t.visible ? styles.toastEnter : styles.toastLeave}`}>
              <div className={styles.toastIcon}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                  <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
                  <path d="M13.73 21a2 2 0 01-3.46 0" />
                </svg>
              </div>
              <div className={styles.toastText}>{formatMessage(normalizada.mensaje)}</div>
            </div>
          ), {
            duration: 4000,
            position: 'bottom-right',
          });

          setNotificaciones(prev => {
            if (prev.some(n => n.id === normalizada.id)) return prev;
            return [normalizada, ...prev];
          });
        });
      },
    });

    client.activate();
    return () => client.deactivate();
  }, []);

  // 4. Cerrar al hacer clic fuera
  useEffect(() => {
    function handleClickOutside(e) {
      if (notifRef.current && !notifRef.current.contains(e.target)) {
        setShowNotifications(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleMarcarLeidas = async () => {
    setNotificaciones(prev => prev.map(n => ({ ...n, leida: true, destacada: false })));
    const token = sessionStorage.getItem("token");
    try {
      await fetch('/pedidoMs/notificaciones/leer-todas', {
        method: 'PATCH',
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (e) { console.error(e); }
  };

  const formatMessage = (text) => {
    if (!text) return text;
    const baseStatuses = ["RECHAZADO", "ACEPTADO", "EN_PREPARACION", "EN_ENVIO", "ENTREGADO", "EN_ESPERA", "REALIZADO", "PENDIENTE", "PAGADO", "APROBADO", "EN_CAMINO", "CANCELADO"];
    
    const statuses = baseStatuses.flatMap((s) => {
      if (!s.includes("_")) return [s];
      const spaced = s.replaceAll("_", " ");
      const dashed = s.replaceAll("_", "-");
      return [s, spaced, dashed];
    });

    const regex = new RegExp(`(${statuses.join('|')})`, 'gi');
    return text.split(regex).map((part, i) => 
      statuses.some(s => s.toLowerCase() === part.toLowerCase()) 
        ? <span key={i} className={styles.statusHighlight}>{part}</span> 
        : part
    );
  };

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
            <div className={styles.navbarIconWrapper} ref={notifRef}>
              <button
                className={styles.iconBtn}
                onClick={(e) => {
                  e.stopPropagation()
                  setShowNotifications(!showNotifications)
                  setShowUserMenu(false)
                }}
              >
                <div className={styles.notifBadgeWrapper}>
                  <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                    <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
                    <path d="M13.73 21a2 2 0 01-3.46 0" />
                  </svg>
                  {unreadCount > 0 && <span className={styles.badge}>{unreadCount}</span>}
                </div>
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
                    <button onClick={handleMarcarLeidas} className={styles.clearBtn}>Marcar como leidas</button>
                  </div>
                  <div className={styles.notifList}>
                    {notificaciones.length > 0 ? (
                      notificaciones.map(n => (
                        <div key={n.id} className={`${styles.notifItem} ${(!n.leida || n.destacada) ? styles.unreadLine : ''}`}>
                          <p>{formatMessage(n.mensaje)}</p>
                          <small>{new Date(n.fechaHora).toLocaleString()}</small>
                        </div>
                      ))
                    ) : (
                      <div className={styles.emptyNotif}>
                        <strong>No tenes notificaciones</strong>
                        <p>¡Tus ventas aparecerán aquí!</p>
                      </div>
                    )}
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
                      <Link href="/" className={styles.logoutLink} onClick={handleLogout}>
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" /><polyline points="16 17 21 12 16 7" /><line x1="21" y1="12" x2="9" y2="12" /></svg>
                        Salir
                      </Link>
                    </li>

                  </ul>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
      <Toaster 
        position="bottom-right"
        containerStyle={{ zIndex: 99999 }}
      />
    </>
  )
}
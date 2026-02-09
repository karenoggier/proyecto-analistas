'use client';

import { useState, useRef, useEffect } from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import styles from './Navbar.module.css';

export default function Navbar({ showSearchBar = false }) {
  const [notifOpen, setNotifOpen] = useState(false);
  const [userOpen, setUserOpen] = useState(false);
  const [addressOpen, setAddressOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const pathname = usePathname();

  const notifRef = useRef(null);
  const userRef = useRef(null);
  const addressRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(e) {
      if (notifRef.current && !notifRef.current.contains(e.target)) setNotifOpen(false);
      if (userRef.current && !userRef.current.contains(e.target)) setUserOpen(false);
      if (addressRef.current && !addressRef.current.contains(e.target)) setAddressOpen(false);
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <nav className={styles.navbar}>
      <div className={styles.navLeft}>
        <Link href="/cliente" className={styles.logo}>
          <span className={styles.logoIcon}>🛒</span>
          <span className={styles.logoText}>PediloYa</span>
        </Link>

        <div className={styles.addressSelector} ref={addressRef}>
          <button
            className={styles.addressBtn}
            onClick={() => setAddressOpen(!addressOpen)}
          >
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" />
              <circle cx="12" cy="10" r="3" />
            </svg>
            <span className={styles.addressLabel}>
              <small className={styles.addressSmall}>Enviar a</small>
              Mi direccion
            </span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M6 9l6 6 6-6" />
            </svg>
          </button>

          {addressOpen && (
            <div className={styles.popover}>
              <div className={styles.popoverHeader}>
                <h3>Direcciones</h3>
                <button className={styles.popoverClose} onClick={() => setAddressOpen(false)}>
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M18 6L6 18M6 6l12 12" />
                  </svg>
                </button>
              </div>
              <p className={styles.popoverSubtext}>Elija una direccion...</p>
              <div className={styles.addressList}>
                <label className={styles.addressItem}>
                  <input type="radio" name="address" defaultChecked className={styles.addressRadio} />
                  <div>
                    <strong>Santos Vianni 1032</strong>
                    <span>CP: 3081 - Humboldt, Santa Fe</span>
                  </div>
                </label>
              </div>
              <Link href="/cliente/mis-direcciones" className={styles.addAddressLink} onClick={() => setAddressOpen(false)}>
                + Agregar direccion
              </Link>
            </div>
          )}
        </div>

        {showSearchBar && (
          <div className={styles.searchBarWrapper}>
            <input
              className={styles.searchInput}
              type="text"
              placeholder="Buscar..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <Link href={`/cliente/buscar?q=${encodeURIComponent(searchQuery)}`} className={styles.searchBtn}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
                <circle cx="11" cy="11" r="8" />
                <path d="M21 21l-4.35-4.35" />
              </svg>
            </Link>
          </div>
        )}
      </div>

      <div className={styles.navRight}>
        <div className={styles.iconWrapper} ref={notifRef}>
          <button className={styles.iconBtn} onClick={() => { setNotifOpen(!notifOpen); setUserOpen(false); }}>
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
              <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
              <path d="M13.73 21a2 2 0 01-3.46 0" />
            </svg>
          </button>
          {notifOpen && (
            <div className={styles.popover}>
              <div className={styles.popoverHeader}>
                <div className={styles.popoverHeaderIcon}>
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                    <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
                    <path d="M13.73 21a2 2 0 01-3.46 0" />
                  </svg>
                  <span>Notificaciones</span>
                </div>
              </div>
              <div className={styles.emptyNotif}>
                <strong>No tenes notificaciones</strong>
                <p>Aprovecha para descubrir productos increibles!</p>
              </div>
            </div>
          )}
        </div>

        <Link href="/cliente/carrito" className={styles.iconBtn}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
            <circle cx="9" cy="21" r="1" />
            <circle cx="20" cy="21" r="1" />
            <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6" />
          </svg>
        </Link>

        <div className={styles.iconWrapper} ref={userRef}>
          <button className={styles.userBtn} onClick={() => { setUserOpen(!userOpen); setNotifOpen(false); }}>
            <div className={styles.avatar}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </div>
            <span className={styles.userName}>Nombre</span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M6 9l6 6 6-6" />
            </svg>
          </button>

          {userOpen && (
            <div className={styles.popover}>
              <div className={styles.userMenuHeader}>
                <div className={styles.avatar}>
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                    <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                    <circle cx="12" cy="7" r="4" />
                  </svg>
                </div>
                <span>Nombre y Apellido</span>
              </div>
              <ul className={styles.userMenuList}>
                <li>
                  <Link href="/cliente" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" /><polyline points="9 22 9 12 15 12 15 22" /></svg>
                    Inicio
                  </Link>
                </li>
                <li>
                  <Link href="/cliente" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" /><circle cx="12" cy="7" r="4" /></svg>
                    Mi perfil
                  </Link>
                </li>
                <li>
                  <Link href="/cliente/mis-pedidos" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="1" y="3" width="15" height="13" /><polygon points="16 8 20 8 23 11 23 16 16 16 16 8" /><circle cx="5.5" cy="18.5" r="2.5" /><circle cx="18.5" cy="18.5" r="2.5" /></svg>
                    Mis pedidos
                  </Link>
                </li>
                <li>
                  <Link href="/cliente/mis-direcciones" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" /><circle cx="12" cy="10" r="3" /></svg>
                    Mis direcciones
                  </Link>
                </li>
                <li>
                  <Link href="/" className={styles.logoutLink} onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" /><polyline points="16 17 21 12 16 7" /><line x1="21" y1="12" x2="9" y2="12" /></svg>
                    Salir
                  </Link>
                </li>
              </ul>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

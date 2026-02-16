'use client';

import { useState, useRef, useEffect } from 'react';
import { useRouter } from "next/navigation";
import Link from 'next/link';
import Image from "next/image"
import { usePathname } from 'next/navigation';
import styles from './Navbar.module.css';
import AddressModal from './AddressModal';
import NewAddressModal from './NewAddressModal';

export default function Navbar({ showSearchBar = false, profile, onAddressUpdate, disableAddressModal = false }) {
  const [notifOpen, setNotifOpen] = useState(false);
  const [userOpen, setUserOpen] = useState(false);
  const [addressOpen, setAddressOpen] = useState(false);
  const [newAddressOpen, setNewAddressOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedAddressId, setSelectedAddressId] = useState(null);
  const [showDisabledNotice, setShowDisabledNotice] = useState(false);
  const pathname = usePathname();

  const notifRef = useRef(null);
  const userRef = useRef(null);
  const addressRef = useRef(null);

  const handleLogout = () => {
    sessionStorage.clear()
    window.location.href = "/login"
  }

  const handleSuccessSave = async () => {
    setNewAddressOpen(false);

    if (onAddressUpdate) {
        await onAddressUpdate(); 
    }

    setAddressOpen(true);    
  };

  useEffect(() => {
    function handleClickOutside(e) {
      if (notifRef.current && !notifRef.current.contains(e.target)) setNotifOpen(false);
      if (userRef.current && !userRef.current.contains(e.target)) setUserOpen(false);
      if (addressRef.current && !addressRef.current.contains(e.target)) setAddressOpen(false);
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    const savedId = sessionStorage.getItem("selectedAddressId");
    if (savedId) {
      setSelectedAddressId(savedId);
    }
  }, [profile]);

  useEffect(() => {
    const handleStorageChange = () => {
      setSelectedAddressId(sessionStorage.getItem("selectedAddressId"));
    };

    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  const getDireccionTexto = () => {
    if (!profile || !profile.direcciones || profile.direcciones.length === 0) {
      return "Mi dirección";
    }
    const direccionSeleccionada = profile.direcciones.find(d => d.id === selectedAddressId);
    const direccionAMostrar = direccionSeleccionada || profile.direcciones[0];
    const { calle, numero, localidad } = direccionAMostrar;

    return `${calle} ${numero}, ${localidad}`;
  };

  const handleSearchRedirection = (e) => {
    e.preventDefault();
    const selectedAddressId = sessionStorage.getItem("selectedAddressId");
    if (!selectedAddressId) {
      alert("Por favor, selecciona una dirección antes de buscar.");
      return;
    }

    window.location.href = `/cliente/buscar?q=${encodeURIComponent(searchQuery)}`;
  };

  return (
   <>
    {/* ========== NAVBAR ========== */}
    <nav className={styles.navbar}>
      <div className={styles.navbarInner}>
      <div className={styles.navLeft}>
        <Link href="/cliente" className={styles.logo}>
          <Image src="/logo.png" alt="PediloYa Logo" width={50} height={60} className={styles.logo} priority />
          <span className={styles.logoText}>PediloYa</span>
        </Link>

        <div className={styles.addressSelector} ref={addressRef}>
          <button
            className={styles.addressBtn}
            onClick={() => {
              if (disableAddressModal) {
                setShowDisabledNotice(true);
                setTimeout(() => setShowDisabledNotice(false), 3000);
              } else {
                setAddressOpen(!addressOpen);
              }
            }}
            style={{ cursor: 'pointer' }}
          >
            {/*<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" />
              <circle cx="12" cy="10" r="3" />
            </svg>*/}
            <Image src="/pin-de-ubicacion.png" alt="Pin de ubicación" width={30} height={40} />
            <span className={styles.addressLabel}>
              <small className={styles.addressSmall}>Enviar a</small>
              {getDireccionTexto()}
            </span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M6 9l6 6 6-6" />
            </svg>
          </button>

          {showDisabledNotice && (
            <div style={{
              position: 'absolute',
              top: '110%',
              left: '0',
              backgroundColor: '#ff628e',
              color: '#fff',
              padding: '8px 12px',
              borderRadius: '6px',
              fontSize: '12px',
              zIndex: 1000,
              whiteSpace: 'nowrap',
              boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
            }}>
              No puedes cambiar la dirección en esta página
            </div>
          )}

          <AddressModal 
            isOpen={addressOpen} 
            onClose={() => setAddressOpen(false)} 
            direcciones={profile?.direcciones} 
            onOpenNewAddress={() => {
              setAddressOpen(false); 
              setNewAddressOpen(true);
            }}
          />

          <NewAddressModal 
            isOpen={newAddressOpen}
            onClose={handleSuccessSave}
            onSuccess={handleSuccessSave}
          />

          {/*{addressOpen && (
            <div className={styles.modalOverlay} onClick={() => setAddressOpen(false)}>
              <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
                <div className={styles.popoverHeader}>
                  <h3>Direcciones</h3>
                  <button className={styles.popoverClose} onClick={() => setAddressOpen(false)}>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                      <path d="M18 6L6 18M6 6l12 12" />
                    </svg>
                  </button>
                </div>
                <p className={styles.popoverSubtext}>Elija una direccion...</p>

                <div className={styles.addressContainer}>
                  <div className={styles.addressList}>
                    <label className={styles.addressItem}>
                      <input type="radio" name="address" defaultChecked className={styles.addressRadio} />
                      <div className={styles.addressText}>
                        <strong>Santos Vianni 1032</strong>
                        <span>CP: 3081 - Humboldt, Santa Fe</span>
                      </div>
                    </label>
                    {/* Aquí podrías mapear más direcciones 
                  </div>

                  <Link href="/cliente/direcciones" className={styles.addAddressLink} onClick={() => setAddressOpen(false)}>
                    <span className={styles.plusIcon}>+</span> Agregar dirección
                  </Link>
                </div>
              </div>
            </div>
          )}*/}
        </div>

        {showSearchBar && (
          <div className={styles.searchBarWrapper}>
            <input
              className={styles.searchInput}
              type="text"
              placeholder="Buscar..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearchRedirection(e)}
            />
            <button onClick={handleSearchRedirection} className={styles.searchBtn}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="white">
                  <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
              </svg>
            </button>
          </div>
        )}
      </div>

      <div className={styles.navRight}>
        <div className={styles.iconWrapper} ref={notifRef}>
          <button className={styles.iconBtn} onClick={() => { setNotifOpen(!notifOpen); setUserOpen(false); }}>
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
              <path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9" />
              <path d="M13.73 21a2 2 0 01-3.46 0" />
            </svg>
          </button>
          {notifOpen && (
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

        <Link href="/cliente/carrito" className={styles.iconBtn}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
            <circle cx="9" cy="21" r="1" />
            <circle cx="20" cy="21" r="1" />
            <path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6" />
          </svg>
        </Link>

        <div className={styles.iconWrapper} ref={userRef}>
          <button className={styles.userBtn} onClick={() => { setUserOpen(!userOpen); setNotifOpen(false); }}>
            <div className={styles.avatar}>
              {/*<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>*/}
              <Image src="/perfil.png" alt="Foto de perfil" width={35} height={45} />
            </div>
            <span className={styles.userName}>{profile?.nombre}</span>
            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M6 9l6 6 6-6" />
            </svg>
          </button>

          {userOpen && (
            <div className={styles.popover}>
              <div className={styles.userMenuHeader}>
                <div className={styles.avatar}>
                  <Image src="/perfil.png" alt="Foto de perfil" width={35} height={45} />
                </div>
                <span>{profile ? `${profile.nombre} ${profile.apellido}` : "Cargando..."}</span>
              </div>
              <ul className={styles.userMenuList}>
                <li>
                  <Link href="/cliente" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" /><polyline points="9 22 9 12 15 12 15 22" /></svg>
                    Inicio
                  </Link>
                </li>
                <li>
                  <div className={styles.disabledLinkContent}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" /><circle cx="12" cy="7" r="4" /></svg>
                    Mi perfil
                    <span className={styles.comingSoon}>Próximamente</span>
                  </div>
                </li>
                <li>
                  <Link href="/cliente/pedidos" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="1" y="3" width="15" height="13" /><polygon points="16 8 20 8 23 11 23 16 16 16 16 8" /><circle cx="5.5" cy="18.5" r="2.5" /><circle cx="18.5" cy="18.5" r="2.5" /></svg>
                    Mis pedidos
                  </Link>
                </li>
                <li>
                  <Link href="/cliente/direcciones" onClick={() => setUserOpen(false)}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" /><circle cx="12" cy="10" r="3" /></svg>
                    Mis direcciones
                  </Link>
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
    </>
  );
}

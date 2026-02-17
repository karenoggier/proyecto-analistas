'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

export default function Paso2Page() {
  const [deliveryType, setDeliveryType] = useState('address');
  const [clientProfile, setClientProfile] = useState(null);
  
  useEffect(() => {
    fetchPerfil();
  }, []);
    
  const fetchPerfil = async () => {
    const token = sessionStorage.getItem("token")
    const rol = sessionStorage.getItem("rol")
      
    if (!token || rol !== "CLIENTE") {
      window.location.href = "/login"
      return
    }
      try {
        const headers = {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        };
      
        const [perfilRes] = await Promise.all([
          fetch('/pedidoMs/clientes/perfil', { method: 'GET', headers }),
        ]);
      
        if (perfilRes.status === 401 || perfilRes.status === 403) {
          sessionStorage.clear(); 
          window.location.href = "/login?expired=true"; 
          return;
        }
      
        if (perfilRes.ok) {
          const dataPerfil = await perfilRes.json();
          setClientProfile(dataPerfil);
        } else {
            console.error("Error al obtener perfil del cliente");
        }
      
        } catch (error) {
          console.error("Error de red:", error);
    } 
  }

  const handleRefreshProfile = () => {
    fetchPerfil();
  };
  

  return (
    <div className={styles.page}>
      <Navbar profile={clientProfile} onAddressUpdate={handleRefreshProfile} disableAddressModal={true}/>

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/proceso-pedido/paso1" className={styles.backBtn}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
               <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={2} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            <h2 className={styles.sectionTitle}>Direccion de entrega</h2>
            <p className={styles.sectionSubtitle}>Elija una direccion...</p>

            <div style={{ border: '1px solid #e5e7eb', borderRadius: '8px', marginBottom: '1rem', overflow: 'hidden' }}>
              <label className={styles.addressOption} onClick={() => setDeliveryType('address')} style={{ border: 'none', marginBottom: 0, borderRadius: 0 }}>
                <input
                  type="radio"
                  name="delivery"
                  value="address"
                  checked={deliveryType === 'address'}
                  onChange={() => setDeliveryType('address')}
                  className={styles.addressRadio}
                />
                <div className={styles.addressInfo}>
                  <strong>Santos Vianni 1032</strong>
                  <span>CP: 3081 - Humboldt, Santa Fe</span>
                </div>
              </label>
              <div style={{ height: '1px', backgroundColor: '#e5e7eb', width: '100%' }}></div>
              <Link href="/cliente/direcciones" className={styles.addressOption} style={{ textDecoration: 'none', border: 'none', marginBottom: 0, borderRadius: 0, color: '#e84c6a' }}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
                </svg>
                <div className={styles.addressInfo}>
                  <p>Cambiar dirección</p>
                </div>
              </Link>
            </div>

            <label className={styles.retiroOption} onClick={() => setDeliveryType('pickup')}>
              <input
                type="radio"
                name="delivery"
                value="pickup"
                checked={deliveryType === 'pickup'}
                onChange={() => setDeliveryType('pickup')}
                className={styles.addressRadio}
              />
              <span style={{ fontSize: '14px', color: '#222' }}>Retiro en local</span>
            </label>
          </div>
          <ResumenCompra />
        </div>

        <div className={styles.continueBtnWrapper}>
          <Link href="/cliente/proceso-pedido/paso3" className={styles.continueBtn}>
            Continuar
          </Link>
        </div>
      </main>

      <Footer />
    </div>
  );
}

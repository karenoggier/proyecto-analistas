'use client';

import Image from 'next/image';
import { useState, useEffect } from 'react';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import styles from '../proceso-pedido.module.css';

export default function Paso5Page() {
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
          <Link href="/cliente" className={styles.backBtn}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
               <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={5} />

        <div className={styles.successPage}>
          <Image
            src="/cliente/chica-ok.png"
            alt="Pedido exitoso"
            width={240}
            height={240}
            className={styles.successImg}
          />

          <h2 className={styles.successTitle}>Pedido realizado con exito!</h2>
          <p className={styles.successSubtitle}>Tu pedido ya se esta preparando</p>

          <div className={styles.successTimeCard}>
            <div className={styles.successTimeIcon}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                <circle cx="12" cy="12" r="10" />
                <polyline points="12 6 12 12 16 14" />
              </svg>
            </div>
            <div className={styles.successTimeText}>
              Llegara en aproximadamente<br />30-45 min
            </div>
          </div>

          <div className={styles.successActions}>
            <Link href="/cliente/pedidos" className={styles.successPrimaryBtn}>
              Ir a mis pedidos
            </Link>
            <Link href="/cliente" className={styles.successSecondaryBtn}>
              Volver al inicio
            </Link>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
}

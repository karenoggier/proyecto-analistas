'use client';

import { useState, useEffect, Suspense } from 'react';
import { useSearchParams } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import confetti from 'canvas-confetti';

import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import styles from '../proceso-pedido.module.css';

function Paso5Content() {
    const searchParams = useSearchParams();
    
    const status = searchParams.get('status'); 
    const paymentId = searchParams.get('payment_id');
    const externalReference = searchParams.get('external_reference'); 

    useEffect(() => {
        if (status === 'approved') {
            dispararConfeti();
        }
    }, [status]);

    const dispararConfeti = () => {
        confetti({
            particleCount: 150,
            spread: 70,
            origin: { y: 0.6 },
            colors: ['#e84c6a', '#22c55e', '#ffffff']
        });
    };

    const renderStatusContent = () => {
        switch (status) {
            case 'approved':
                return (
                    <>
                        <Image src="/cliente/chica-ok.png" alt="Éxito" width={240} height={240} className={styles.successImg} />
                        <h2 className={styles.successTitle}>¡Pago aprobado!</h2>
                        <p className={styles.successSubtitle}>Tu pedido <b>#{externalReference}</b> ya se está preparando.</p>
                    </>
                );
            case 'pending':
                return (
                    <>
                        <div className={styles.pendingIcon}>⏳</div>
                        <h2 className={styles.successTitle}>Pago pendiente</h2>
                        <p className={styles.successSubtitle}>Estamos esperando la confirmación de tu pago (ID: {paymentId}).</p>
                        <p className={styles.infoText}>El restaurante iniciará el pedido cuando el pago se acredite.</p>
                    </>
                );
            case 'failure':
            default:
                return (
                    <>
                        <div className={styles.errorIcon}>❌</div>
                        <h2 className={styles.errorTitle}>No pudimos procesar el pago</h2>
                        <p className={styles.successSubtitle}>Hubo un error con tu tarjeta o la operación fue cancelada.</p>
                    </>
                );
        }
    };

    return (
        <div className={styles.successPage}>
            {renderStatusContent()}
            
            <div className={styles.successActions}>
                {status !== 'approved' && status !== 'pending' ? (
                    <Link href="/cliente/proceso-pedido/paso4" className={styles.successPrimaryBtn}>Intentar nuevamente</Link>
                ) : (
                    <Link href="/cliente/pedidos" className={styles.successPrimaryBtn}>Ir a mis pedidos</Link>
                )}
                <Link href="/cliente" className={styles.successSecondaryBtn}>Volver al inicio</Link>
            </div>
        </div>
    );
}

export default function Paso5Page() {
    const [clientProfile, setClientProfile] = useState(null);

    useEffect(() => {
        fetchPerfil();
    }, []);

    const fetchPerfil = async () => {
        const token = sessionStorage.getItem("token");
        const rol = sessionStorage.getItem("rol");
        
        if (!token || rol !== "CLIENTE") {
            window.location.href = "/login";
            return;
        }

        try {
            const res = await fetch('/pedidoMs/clientes/perfil', { 
                headers: { 'Authorization': `Bearer ${token}` } 
            });
            if (res.ok) {
                const data = await res.json();
                setClientProfile(data);
            }
        } catch (error) {
            console.error("Error de red:", error);
        }
    };

    const handleRefreshProfile = () => {
        fetchPerfil();
    };
    
    return (
        <div className={styles.page}>
            <Navbar profile={clientProfile} onAddressUpdate={handleRefreshProfile} disableAddressModal={true}/>
            <main className={styles.main}>
                <div className={styles.header}>
                    <h1 className={styles.title}>ESTADO DEL PEDIDO</h1>
                </div>
                <Stepper activeStep={5} />
                {/* Suspense es obligatorio en Next.js para usar useSearchParams */}
                <Suspense fallback={<div>Cargando estado del pago...</div>}>
                    <Paso5Content />
                </Suspense>
            </main>
            <Footer />
        </div>
    );
}
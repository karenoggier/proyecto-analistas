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
    const [clientProfile, setClientProfile] = useState(null);
    const searchParams = useSearchParams();
    
    // Capturamos los datos de Mercado Pago de la URL
    const status = searchParams.get('status'); // approved, pending, failure
    const paymentId = searchParams.get('payment_id');
    const externalReference = searchParams.get('external_reference'); // Es tu pedidoId

    useEffect(() => {
        fetchPerfil();
        // Si el pago fue exitoso, tiramos confeti
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

    const fetchPerfil = async () => {
        const token = sessionStorage.getItem("token");
        if (!token) return;
        try {
            const res = await fetch('/pedidoMs/clientes/perfil', {
                method: 'GET',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (res.ok) setClientProfile(await res.json());
        } catch (error) {
            console.error("Error al obtener perfil:", error);
        }
    };

    // Lógica de renderizado según el estado
    const renderStatusContent = () => {
        switch (status) {
            case 'approved':
                return (
                    <>
                        <Image src="/cliente/chica-ok.png" alt="Éxito" width={240} height={240} className={styles.successImg} />
                        <h2 className={styles.successTitle}>¡Pago aprobado!</h2>
                        <p className={styles.successSubtitle}>Tu pedido <b>#{externalReference}</b> ya se está preparando.</p>
                        <div className={styles.successTimeCard}>
                            <div className={styles.successTimeText}>Llegará en aproximadamente<br />30-45 min</div>
                        </div>
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
                        <p className={styles.successSubtitle}>Hubo un error con tu tarjeta o la operación fue cancelated.</p>
                        <Link href="/cliente/proceso-pedido/paso4" className={styles.successPrimaryBtn}>
                            Intentar con otro método
                        </Link>
                    </>
                );
        }
    };

    return (
        <div className={styles.successPage}>
            {renderStatusContent()}
            
            <div className={styles.successActions}>
                <Link href="/cliente/pedidos" className={styles.successPrimaryBtn}>Ir a mis pedidos</Link>
                <Link href="/cliente" className={styles.successSecondaryBtn}>Volver al inicio</Link>
            </div>
        </div>
    );
}

export default function Paso5Page() {
    return (
        <div className={styles.page}>
            <Navbar disableAddressModal={true} />
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
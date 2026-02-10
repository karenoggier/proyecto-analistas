'use client';

import { useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

export default function Paso4Page() {
  const [paymentMethod, setPaymentMethod] = useState('mercadopago');

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/proceso-pedido/paso3" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={4} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            <h2 className={styles.sectionTitle}>Medio de pago</h2>
            <p className={styles.sectionSubtitle}>Elija un medio de pago...</p>

            <div className={styles.paymentOption}>
              <input
                type="radio"
                name="payment"
                value="mercadopago"
                checked={paymentMethod === 'mercadopago'}
                onChange={() => setPaymentMethod('mercadopago')}
                className={styles.addressRadio}
              />
              <div className={styles.paymentContent}>
                <div className={styles.paymentHeader}>
                  <span className={styles.paymentName}>Mercado Pago</span>
                  <Image
                    src="/images/mercadopago-logo.jpg"
                    alt="Mercado Pago"
                    width={60}
                    height={40}
                    className={styles.paymentLogo}
                  />
                </div>
                <p className={styles.paymentDesc}>
                  La plataforma de pago mas confiable de Latinoamerica.
                  Usa tus tarjetas de credito y debito.
                </p>
                <p className={styles.paymentDesc}>
                  Al presionar el boton de abajo, sera redirigido a la
                  aplicacion de <em>Mercado Pago</em> para completar tu pago de
                  forma segura. Una vez finalizado, volveras
                  automaticamente a nuestra aplicacion.
                </p>

                <Link href="/cliente/proceso-pedido/paso5" className={styles.mercadoPagoBtn}>
                  <Image src="/images/mercadopago-logo.jpg" alt="" width={80} height={20} className={styles.mercadoPagoBtnImg} />
                  mercado pago
                </Link>
                <p className={styles.paymentSecure}>Paga de forma segura</p>
              </div>
            </div>
          </div>
          <ResumenCompra />
        </div>
      </main>

      <Footer />
    </div>
  );
}

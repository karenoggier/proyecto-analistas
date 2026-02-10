'use client';

import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import styles from '../proceso-pedido.module.css';

export default function Paso5Page() {
  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={5} />

        <div className={styles.successPage}>
          <Image
            src="/images/success-girl.jpg"
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

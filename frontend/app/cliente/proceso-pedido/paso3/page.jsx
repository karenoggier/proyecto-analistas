'use client';

import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from './../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

export default function Paso3Page() {
  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/proceso-pedido/paso2" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={3} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            <h2 className={styles.sectionTitle}>Datos personales</h2>

            <div className={styles.datosCard}>
              <p className={styles.datosRow}><strong>Nombre:</strong> Karen</p>
              <p className={styles.datosRow}><strong>Apellido:</strong> Oggier</p>
              <p className={styles.datosRow}><strong>Telefono:</strong> 3496-511096</p>
              <p className={styles.datosRow}><strong>Email:</strong> karenoggier@gmail.com</p>
            </div>
          </div>
          <ResumenCompra />
        </div>

        <div className={styles.continueBtnWrapper}>
          <Link href="/cliente/proceso-pedido/paso4" className={styles.continueBtn}>
            Continuar
          </Link>
        </div>
      </main>

      <Footer />
    </div>
  );
}

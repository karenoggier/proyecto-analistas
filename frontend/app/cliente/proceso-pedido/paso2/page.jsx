'use client';

import { useState } from 'react';
import Link from 'next/link';
import Navbar from '../../components/Navbar';
import Footer from './../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

export default function Paso2Page() {
  const [deliveryType, setDeliveryType] = useState('address');

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href="/cliente/proceso-pedido/paso1" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={2} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            <h2 className={styles.sectionTitle}>Direccion de entrega</h2>
            <p className={styles.sectionSubtitle}>Elija una direccion...</p>

            <label className={styles.addressOption} onClick={() => setDeliveryType('address')}>
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

            <Link href="/cliente/mis-direcciones" className={styles.changeAddressLink}>
              Cambiar direccion
            </Link>

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

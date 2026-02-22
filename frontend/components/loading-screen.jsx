import React from 'react';
import styles from './loading-screen.module.css';

export default function LoadingScreen({ text = "Cargando..." }) {
  return (
    <div className={styles.container}>
      <div className={styles.loader}></div>
      <p className={styles.text}>{text}</p>
    </div>
  );
}
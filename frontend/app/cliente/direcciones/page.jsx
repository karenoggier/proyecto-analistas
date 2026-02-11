'use client';

import { useState } from 'react';
import Link from 'next/link';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './direcciones.module.css';
import NewAddressModal from '../components/NewAddressModal';

const provincias = [
  'Buenos Aires', 'CABA', 'Catamarca', 'Chaco', 'Chubut', 'Cordoba',
  'Corrientes', 'Entre Rios', 'Formosa', 'Jujuy', 'La Pampa', 'La Rioja',
  'Mendoza', 'Misiones', 'Neuquen', 'Rio Negro', 'Salta', 'San Juan',
  'San Luis', 'Santa Cruz', 'Santa Fe', 'Santiago del Estero',
  'Tierra del Fuego', 'Tucuman',
];

const localidades = [
  'Humboldt', 'Esperanza', 'Rafaela', 'Santa Fe', 'Rosario',
  'Reconquista', 'Venado Tuerto',
];

export default function MisDireccionesPage() {
  const [showModal, setShowModal] = useState(false);
  const [deleteMode, setDeleteMode] = useState(false);
  const [addresses, setAddresses] = useState([
    {
      id: 1,
      calle: 'Santos Vianni',
      numero: '1032',
      cp: '3081',
      localidad: 'Humboldt',
      provincia: 'Santa Fe',
      notas: '',
    },
  ]);

  const [form, setForm] = useState({
    provincia: '',
    localidad: '',
    calle: '',
    numero: '',
    cp: '',
    notas: '',
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.calle || !form.numero) return;
    const newAddress = {
      id: Date.now(),
      calle: form.calle,
      numero: form.numero,
      cp: form.cp,
      localidad: form.localidad,
      provincia: form.provincia,
      notas: form.notas,
    };
    setAddresses([...addresses, newAddress]);
    setForm({ provincia: '', localidad: '', calle: '', numero: '', cp: '', notas: '' });
    setShowModal(false);
  };

  const handleAddNewAddress = (formData) => {
    const newAddress = {
      id: Date.now(),
      ...formData
    };
    setAddresses([...addresses, newAddress]);
    setShowModal(false);
    // Aquí podrías agregar el fetch para guardar en el backend (ms-pedido / puerto 5433)
  };

  const handleDelete = (id) => {
    setAddresses(addresses.filter((a) => a.id !== id));
  };

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
          <h1 className={styles.title}>MIS DIRECCIONES</h1>
        </div>

        <div className={styles.actions}>
          <button className={styles.addBtn} onClick={() => setShowModal(true)}>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            Agregar direccion
          </button>
          <button
            className={styles.deleteBtn}
            onClick={() => setDeleteMode(!deleteMode)}
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
              <polyline points="3 6 5 6 21 6" />
              <path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2" />
            </svg>
            Eliminar direccion
          </button>
        </div>

        <div className={styles.addressList}>
          {addresses.map((addr) => (
            <div key={addr.id} className={styles.addressCard}>
              <div className={styles.addressRadioWrapper}>
                {deleteMode ? (
                  <button
                    className={styles.deleteItemBtn}
                    onClick={() => handleDelete(addr.id)}
                    aria-label="Eliminar direccion"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2">
                      <line x1="18" y1="6" x2="6" y2="18" />
                      <line x1="6" y1="6" x2="18" y2="18" />
                    </svg>
                  </button>
                ) : (
                  <div className={styles.radioChecked} />
                )}
              </div>
              <div className={styles.addressDetails}>
                <strong>{addr.calle} {addr.numero}</strong>
                <span>CP: {addr.cp} - {addr.localidad}, {addr.provincia}</span>
              </div>
            </div>
          ))}
        </div>
      </main>

      <Footer />

      {/* New Address Modal */}
      <NewAddressModal 
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onSubmit={handleAddNewAddress}
        provincias={provincias}
        localidades={localidades}
      />


      
    </div>
  );
}

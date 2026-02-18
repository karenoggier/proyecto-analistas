'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter, useSearchParams } from 'next/navigation';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import Stepper from '../../components/Stepper';
import ResumenCompra from '../../components/ResumenCompra';
import styles from '../proceso-pedido.module.css';

export default function Paso2Page() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [vendedorId, setVendedorId] = useState(searchParams.get('vendedorId'));
  const [deliveryType, setDeliveryType] = useState('address');
  const [clientProfile, setClientProfile] = useState(null);
  const [direcciones, setDirecciones] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState(null);
  const [costoEnvio, setCostoEnvio] = useState(null);
  const [vendorRealizaEnvios, setVendorRealizaEnvios] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const paramId = searchParams.get('vendedorId');
    if (paramId) {
      setVendedorId(paramId);
      sessionStorage.setItem("currentVendedorId", paramId);
    } else {
      const stored = sessionStorage.getItem("currentVendedorId");
      if (stored) setVendedorId(stored);
    }
  }, [searchParams]);

  useEffect(() => {
    fetchPerfil();
  }, []);

  useEffect(() => {
    if (clientProfile && vendedorId) {
      fetchDireccionesValidas();
    }
  }, [clientProfile, vendedorId]);
    
  useEffect(() => {
    if (deliveryType === 'pickup') {
      setCostoEnvio(0);
    } else if (deliveryType === 'address' && selectedAddressId && vendedorId) {
      setCostoEnvio(null); 
      fetchCostoEnvio();
    }
  }, [deliveryType, selectedAddressId, vendedorId]);

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

  const fetchDireccionesValidas = async () => {
    try {
      const token = sessionStorage.getItem("token");
      
      const resVendedor = await fetch(`/catalogoMs/api/vendedores/perfil-publico/${vendedorId}`, {
         headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (!resVendedor.ok) {
        console.error("Error al obtener datos del vendedor");
        return;
      }
      const dataVendedor = await resVendedor.json();
      
      setVendorRealizaEnvios(dataVendedor.realizaEnvios);
      if (!dataVendedor.realizaEnvios) {
        setDeliveryType('pickup');
        return;
      }

      let localidadRaw = dataVendedor.direccion?.localidad;
      let nombreLocalidad = "";

      if (typeof localidadRaw === 'string') {
        nombreLocalidad = localidadRaw;
      } else if (typeof localidadRaw === 'object' && localidadRaw !== null) {
        nombreLocalidad = localidadRaw.nombre || localidadRaw.localidad || "";
      }

      if (!nombreLocalidad || !nombreLocalidad.trim()) {
        console.warn("El vendedor no tiene localidad definida o válida.");
        return;
      }
      nombreLocalidad = nombreLocalidad.trim();

      const resDir = await fetch(`/pedidoMs/direcciones/filtrar?localidad=${encodeURIComponent(nombreLocalidad)}`, {
          headers: { 'Authorization': `Bearer ${token}` }
      });

      if (resDir.ok) {
          const dataDir = await resDir.json();
          const direccionesList = Array.isArray(dataDir) ? dataDir : [];
          setDirecciones(direccionesList);

          const currentId = sessionStorage.getItem("selectedAddressId");
          const isValid = direccionesList.find(d => d.id === currentId);

          if (!isValid && direccionesList.length > 0) {
              const newDefault = direccionesList[0];
              handleSelectAddress(newDefault.id);
          } else if (isValid) {
              setSelectedAddressId(currentId);
          }
      }
    } catch (error) {
      console.error("Error cargando direcciones:", error);
    }
  };

  const fetchCostoEnvio = async () => {
    try {
      const token = sessionStorage.getItem("token");
      const res = await fetch(`/pedidoMs/pedidos/costo-envio/${vendedorId}/${selectedAddressId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (res.ok) {
        const data = await res.json();
        setCostoEnvio(data);
      } else {
        console.error("Error al obtener costo de envío");
      }
    } catch (error) {
      console.error("Error de red al obtener costo de envío:", error);
    }
  };

  const handleRefreshProfile = () => {
    fetchPerfil();
  };
  
  const handleSelectAddress = (id) => {
    setDeliveryType('address');
    setSelectedAddressId(id);
    sessionStorage.setItem("selectedAddressId", id);
    window.dispatchEvent(new Event('storage'));
  };

  const handleContinue = async () => {

    // Si es entrega a domicilio, validar que haya dirección seleccionada
    if (deliveryType === 'address' && !selectedAddressId) {
      alert("Por favor selecciona una dirección");
      return;
    }

    setSaving(true);
    try {
      const token = sessionStorage.getItem("token");
      const pedidoId = sessionStorage.getItem("currentPedidoId");

      if (!pedidoId) {
        alert("Error: No hay pedido en sesión. Vuelve al paso anterior.");
        setSaving(false);
        return;
      }

      const res = await fetch('/pedidoMs/pedidos/confirmar-envio', {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          vendedorId: vendedorId,
          idDireccion: deliveryType === 'address' ? selectedAddressId : null,
          metodoEnvio: deliveryType === 'address' ? 'ENVIO_A_DOMICILIO' : 'RETIRO_EN_LOCAL'
        })
      });

      if (!res.ok) {
        console.error("Error al guardar envío:", res.status);
        alert("Error al guardar dirección");
        setSaving(false);
        return;
      }

      const respodidoData = await res.json();

      // Navegar al paso siguiente
      router.push(`/cliente/proceso-pedido/paso3?vendedorId=${vendedorId}`);
    } catch (error) {
      console.error("Error de red:", error);
      alert("Error de conexión. Intenta de nuevo.");
      setSaving(false);
    }
  };

  return (
    <div className={styles.page}>
      <Navbar profile={clientProfile} onAddressUpdate={handleRefreshProfile} disableAddressModal={true}/>

      <main className={styles.main}>
        <div className={styles.header}>
          <Link href={`/cliente/proceso-pedido/paso1?vendedorId=${vendedorId}`} className={styles.backBtn}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
               <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
            </svg>
          </Link>
          <h1 className={styles.title}>PROGRESO DE PEDIDO</h1>
        </div>

        <Stepper activeStep={2} />

        <div className={styles.contentRow}>
          <div className={styles.contentLeft}>
            {vendorRealizaEnvios ? (
              <>
                <h2 className={styles.sectionTitle}>Direccion de entrega</h2>
                <p className={styles.sectionSubtitle}>Elija una direccion...</p>

                <div style={{ border: '1px solid #e5e7eb', borderRadius: '8px', marginBottom: '1rem', overflow: 'hidden' }}>
                  {direcciones.length > 0 ? (
                    direcciones.map((dir, index) => (
                      <div key={dir.id}>
                        <label 
                          className={styles.addressOption} 
                          onClick={() => handleSelectAddress(dir.id)} 
                          style={{ border: 'none', marginBottom: 0, borderRadius: 0, cursor: 'pointer' }}
                        >
                          <input
                            type="radio"
                            name="deliveryAddress"
                            checked={deliveryType === 'address' && selectedAddressId === dir.id}
                            onChange={() => handleSelectAddress(dir.id)}
                            className={styles.addressRadio}
                          />
                          <div className={styles.addressInfo}>
                            <strong>{dir.calle} {dir.numero}</strong>
                            <span>CP: {dir.codigoPostal} - {dir.localidad}, {dir.provincia}</span>
                          </div>
                        </label>
                        {index < direcciones.length - 1 && <div style={{ height: '1px', backgroundColor: '#e5e7eb', width: '100%' }}></div>}
                      </div>
                    ))
                  ) : (
                    <div style={{ padding: '1rem', color: '#666', fontSize: '14px' }}>
                      No tienes direcciones registradas en la localidad del vendedor.
                    </div>
                  )}
                  <div style={{ height: '1px', backgroundColor: '#e5e7eb', width: '100%' }}></div>
                  <Link href="/cliente/direcciones" className={styles.addressOption} style={{ textDecoration: 'none', border: 'none', marginBottom: 0, borderRadius: 0, color: '#e84c6a' }}>
                    <div className={styles.addressInfo}>
                      <p>+ Añadir dirección</p>
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
              </>
            ) : (
              <>
                <h2 className={styles.sectionTitle}>Modo de entrega</h2>
                <div className={styles.retiroOption} style={{ cursor: 'default', backgroundColor: '#fef0f2', borderColor: '#ff4b7e' }}>
                   <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ff4b7e" strokeWidth="2" style={{marginRight: '10px'}}>
                      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" />
                      <circle cx="12" cy="10" r="3" />
                   </svg>
                   <span style={{ fontSize: '14px', color: '#222' }}>
                     Este vendedor solo admite <strong>Retiro en local</strong>.
                   </span>
                </div>
              </>
            )}
          </div>
          <ResumenCompra realizaEnvios={deliveryType === 'address'} costoEnvio={costoEnvio} />
        </div>

        <div className={styles.continueBtnWrapper}>
          <button onClick={handleContinue} className={styles.continueBtn} disabled={saving}>
            {saving ? 'Guardando...' : 'Continuar'}
          </button>
        </div>
      </main>

      <Footer />
    </div>
  );
}

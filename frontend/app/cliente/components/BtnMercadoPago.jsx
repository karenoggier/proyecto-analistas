'use client';

import { initMercadoPago, Wallet } from '@mercadopago/sdk-react';
import { useEffect } from 'react';

export default function BtnMercadoPago({ preferenceId }) {
  
  useEffect(() => {
    // Inicializamos con tu Public Key
    initMercadoPago('APP_USR-d1310429-98f9-47e2-88a2-55fccc1ae826', {
      locale: 'es-AR'
    });
  }, []);

  if (!preferenceId) return null;

  return (
    <div className="wallet-container">
      <Wallet 
        initialization={{ preferenceId: preferenceId, redirectMode: 'modal' }} 
        customization={{ texts: { valueProp: 'smart_option' } }}
      />
    </div>
  );
}
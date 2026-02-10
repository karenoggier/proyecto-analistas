'use client';

import { useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import styles from './local.module.css';

const allProducts = [
  {
    id: 1,
    name: 'Doble carne Doble queso + Papas medianas',
    description: 'Hamburguesa doble carne junior 100% vacuna, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompanado de papas medianas.',
    price: 10000,
    img: '/images/producto-burger.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 2,
    name: 'Doble carne Doble queso + Papas medianas',
    description: 'Hamburguesa doble carne junior 100% vacuna, dos fetas de queso cheddar, ketchup, mostaza y cebolla. Acompanado de papas medianas.',
    price: 10000,
    img: '/images/producto-burger.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 3,
    name: 'McPollo + Papas Pequenas',
    description: 'Medallon de pollo frito, mayonesa y lechuga con papas pequenas.',
    price: 7000,
    img: '/images/producto-chicken.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 4,
    name: 'McPollo + Papas Pequenas',
    description: 'Medallon de pollo frito, mayonesa y lechuga con papas pequenas.',
    price: 7000,
    img: '/images/producto-chicken.jpg',
    categoria: 'Comida',
    subcategoria: 'Hamburguesa',
  },
  {
    id: 5,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 6,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 7,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 8,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 9,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
  {
    id: 10,
    name: 'McFlurry Oreo',
    description: 'Helado de vainilla, galletitas oreo en trozos con salsa de chocolate.',
    price: 4000,
    img: '/images/producto-mcflurry.jpg',
    categoria: 'Comida',
    subcategoria: 'Helado',
  },
];

const categorias = ['Comida', 'Bebida'];
const subcategorias = [
  'Hamburguesa', 'Pizza', 'Empanada', 'Milanesa', 'Pasta', 'Parrilla',
  'Ensalada', 'Sushi', 'Helado', 'Postre', 'Celiaco', 'Vegetariano',
  'Gaseosa', 'Agua', 'Cerveza', 'Vino', 'Trago', 'Cafe',
];

export default function LocalPage() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [showFilters, setShowFilters] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [observations, setObservations] = useState('');
  const [cart, setCart] = useState([]);
  const [filterCats, setFilterCats] = useState([]);
  const [filterSubs, setFilterSubs] = useState([]);

  const toggleFilter = (arr, setArr, val) => {
    setArr(arr.includes(val) ? arr.filter((v) => v !== val) : [...arr, val]);
  };

  const clearFilters = () => {
    setFilterCats([]);
    setFilterSubs([]);
  };

  const activeFilterCount = filterCats.length + filterSubs.length;

  const filteredProducts = allProducts.filter((p) => {
    if (filterCats.length > 0 && !filterCats.includes(p.categoria)) return false;
    if (filterSubs.length > 0 && !filterSubs.includes(p.subcategoria)) return false;
    if (searchQuery && !p.name.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    return true;
  });

  const addToCart = () => {
    if (!selectedProduct) return;
    const existing = cart.find((item) => item.id === selectedProduct.id);
    if (existing) {
      setCart(cart.map((item) =>
        item.id === selectedProduct.id
          ? { ...item, qty: item.qty + quantity, obs: observations }
          : item
      ));
    } else {
      setCart([...cart, { ...selectedProduct, qty: quantity, obs: observations }]);
    }
    setSelectedProduct(null);
    setQuantity(1);
    setObservations('');
  };

  const cartSubtotal = cart.reduce((sum, item) => sum + item.price * item.qty, 0);

  return (
    <div className={styles.page}>
      <Navbar />

      <main className={styles.main}>
        {/* Store Header */}
        <div className={styles.storeHeader}>
          <Link href="/cliente/buscar" className={styles.backBtn}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="11" fill="#fef0f2" stroke="#e84c6a" strokeWidth="1.5" />
              <path d="M14 8l-4 4 4 4" stroke="#e84c6a" strokeWidth="2" />
            </svg>
          </Link>
          <div className={styles.storeLogo}>
            <Image src="/images/burger-king-logo.jpg" alt="Burger King" width={100} height={100} className={styles.storeLogoImg} />
          </div>
          <div className={styles.storeInfo}>
            <h1 className={styles.storeName}>Burger King Obelisco</h1>
            <div className={styles.storeDetails}>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" /><circle cx="12" cy="10" r="3" /></svg> Direccion del local</p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg> Abre: 08:00 hs</p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg> Cierra: 21:00 hs</p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></svg> Tiempo estimado de espera: 10-20 min</p>
              <p><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#e84c6a" strokeWidth="2"><rect x="1" y="3" width="15" height="13" /><polygon points="16 8 20 8 23 11 23 16 16 16 16 8" /><circle cx="5.5" cy="18.5" r="2.5" /><circle cx="18.5" cy="18.5" r="2.5" /></svg> Realiza envios a domicilio</p>
            </div>
          </div>
        </div>

        {/* Search + Filter */}
        <div className={styles.searchRow}>
          <div className={styles.searchWrapper}>
            <input
              className={styles.searchInput}
              type="text"
              placeholder="Buscar productos..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button className={styles.searchBtn} aria-label="Buscar">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2">
                <circle cx="11" cy="11" r="8" />
                <path d="M21 21l-4.35-4.35" />
              </svg>
            </button>
          </div>
          <button className={styles.filterBtn} onClick={() => setShowFilters(true)} aria-label="Filtros">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#666" strokeWidth="2">
              <line x1="4" y1="6" x2="20" y2="6" />
              <line x1="4" y1="12" x2="20" y2="12" />
              <line x1="4" y1="18" x2="20" y2="18" />
              <circle cx="8" cy="6" r="2" fill="#666" />
              <circle cx="16" cy="12" r="2" fill="#666" />
              <circle cx="10" cy="18" r="2" fill="#666" />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className={styles.content}>
          <div className={styles.productGrid}>
            {filteredProducts.map((product) => (
              <button
                key={product.id}
                className={styles.productCard}
                onClick={() => { setSelectedProduct(product); setQuantity(1); setObservations(''); }}
              >
                <div className={styles.productInfo}>
                  <h3 className={styles.productName}>{product.name}</h3>
                  <p className={styles.productDesc}>{product.description}</p>
                  <p className={styles.productPrice}>$ {product.price.toLocaleString()}</p>
                </div>
                <div className={styles.productImgWrapper}>
                  <Image src={product.img || "/placeholder.svg"} alt={product.name} width={120} height={120} className={styles.productImg} />
                </div>
              </button>
            ))}
          </div>

          {/* Cart Sidebar */}
          {cart.length > 0 && (
            <aside className={styles.cartSidebar}>
              <div className={styles.cartHeader}>
                <h3>Resumen de carrito</h3>
                <Link href="/cliente/carrito" className={styles.cartEditLink}>Editar</Link>
              </div>
              <div className={styles.cartItems}>
                {cart.map((item) => (
                  <div key={item.id} className={styles.cartItem}>
                    <span className={styles.cartItemQty}>{item.qty} x</span>
                    <span className={styles.cartItemName}>{item.name}</span>
                    <span className={styles.cartItemPrice}>$ {(item.price * item.qty).toLocaleString()}</span>
                  </div>
                ))}
              </div>
              <div className={styles.cartTotal}>
                <span>Subtotal</span>
                <span>${cartSubtotal.toLocaleString()}</span>
              </div>
              <Link href="/cliente/carrito" className={styles.cartGoBtn}>
                Ir al carrito
              </Link>
            </aside>
          )}
        </div>
      </main>

      <Footer />

      {/* Product Detail Modal */}
      {selectedProduct && (
        <div className={styles.modalOverlay} onClick={() => setSelectedProduct(null)}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <button className={styles.modalClose} onClick={() => setSelectedProduct(null)}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>

            <div className={styles.modalImgWrapper}>
              <Image src={selectedProduct.img || "/placeholder.svg"} alt={selectedProduct.name} width={200} height={200} className={styles.modalImg} />
            </div>

            <div className={styles.modalBody}>
              <div className={styles.modalTitleRow}>
                <h2 className={styles.modalTitle}>{selectedProduct.name}</h2>
                <span className={styles.modalPrice}>$ {selectedProduct.price.toLocaleString()}</span>
              </div>
              <p className={styles.modalDesc}>{selectedProduct.description}</p>

              <div className={styles.modalField}>
                <label className={styles.modalLabel}>Unidades</label>
                <div className={styles.qtyControl}>
                  <button
                    className={styles.qtyBtn}
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  >-</button>
                  <span className={styles.qtyValue}>{quantity}</span>
                  <button
                    className={styles.qtyBtn}
                    onClick={() => setQuantity(quantity + 1)}
                  >+</button>
                </div>
              </div>

              <div className={styles.modalField}>
                <label className={styles.modalLabel}>Observaciones</label>
                <textarea
                  className={styles.modalTextarea}
                  value={observations}
                  onChange={(e) => setObservations(e.target.value)}
                  rows={3}
                />
              </div>

              <button className={styles.addToCartBtn} onClick={addToCart}>
                Agregar al carrito
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Filters Modal */}
      {showFilters && (
        <div className={styles.modalOverlay} onClick={() => setShowFilters(false)}>
          <div className={styles.filtersModal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.filtersHeader}>
              <h2>Filtros ({activeFilterCount})</h2>
              <button className={styles.modalClose} onClick={() => setShowFilters(false)}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <line x1="18" y1="6" x2="6" y2="18" />
                  <line x1="6" y1="6" x2="18" y2="18" />
                </svg>
              </button>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3>Categorias</h3>
                <span className={styles.filterMinus}>-</span>
              </div>
              <div className={styles.filterCheckboxes}>
                {categorias.map((cat) => (
                  <label key={cat} className={styles.checkboxLabel}>
                    <input
                      type="checkbox"
                      checked={filterCats.includes(cat)}
                      onChange={() => toggleFilter(filterCats, setFilterCats, cat)}
                      className={styles.checkbox}
                    />
                    {cat}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterSection}>
              <div className={styles.filterSectionHeader}>
                <h3>Subcategorias</h3>
                <span className={styles.filterMinus}>-</span>
              </div>
              <div className={styles.filterSubGrid}>
                {subcategorias.map((sub) => (
                  <label key={sub} className={styles.checkboxLabel}>
                    <input
                      type="checkbox"
                      checked={filterSubs.includes(sub)}
                      onChange={() => toggleFilter(filterSubs, setFilterSubs, sub)}
                      className={styles.checkbox}
                    />
                    {sub}
                  </label>
                ))}
              </div>
            </div>

            <div className={styles.filterActions}>
              <button className={styles.filterApplyBtn} onClick={() => setShowFilters(false)}>Aplicar</button>
              <button className={styles.filterClearBtn} onClick={clearFilters}>Limpiar filtros</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

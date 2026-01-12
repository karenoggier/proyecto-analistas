"use client"

import { useEffect, useRef, useState} from "react"
import Link from "next/link"
import Image from "next/image";
import styles from "./home.module.css"

/* ===================== SCROLL REVEAL COMPONENT ===================== */
function ScrollReveal({
  children,
  direction = "up",
  delay = 0,
  className = "",
}) {
  const ref = useRef(null)
  const [isVisible, setIsVisible] = useState(false)

  useEffect(() => {

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsVisible(true)
          observer.disconnect()
        }
      },
      { threshold: 0.1, rootMargin: "50px 0px -20px 0px" },
    )

    // Small delay to ensure DOM is ready
    const timer = setTimeout(() => {
      if (ref.current) {
        observer.observe(ref.current)
      }
    }, 100)

    return () => {
      clearTimeout(timer)
      observer.disconnect()
    }
  }, [])

  const getBaseClass = () => {
    switch (direction) {
      case "left":
        return styles.revealLeft
      case "right":
        return styles.revealRight
      case "scale":
        return styles.revealScale
      default:
        return styles.revealBase
    }
  }

  const getVisibleClass = () => {
    if (direction === "scale") return styles.revealScaleVisible
    return styles.revealVisible
  }

  const getDelayClass = () => {
    if (delay === 1) return styles.delay1
    if (delay === 2) return styles.delay2
    if (delay === 3) return styles.delay3
    if (delay === 4) return styles.delay4
    if (delay === 5) return styles.delay5
    return ""
  }

  return (
    <div
      ref={ref}
      className={`${getBaseClass()} ${isVisible ? getVisibleClass() : ""} ${getDelayClass()} ${className}`}
    >
      {children}
    </div>
  )
}

/* ===================== MARQUEE COMPONENT (Film Strip Effect) ===================== */
function Marquee({
  children,
  reverse = false,
  className = "",
}) {
  return (
    <div className={className} style={{ overflow: "hidden" }}>
      <div className={reverse ? styles.marqueeTrackReverse : styles.marqueeTrack}>
        {children}
        {children}
      </div>
    </div>
  )
}

/* ===================== DATA ===================== */
const restaurants = [
  { name: "McDonald's", logo: "/mcdonalds.jpeg", colorClass: "mcdonalds" },
  { name: "Mostaza", logo: "/mostaza.jpeg", colorClass: "mostaza" },
  { name: "KFC", logo: "/kfc.jpeg", colorClass: "kfc" },
  { name: "Grido Helados", logo: "/grido.jpeg", colorClass: "grido" },
  { name: "Burger King", logo: "/burger king.jpeg", colorClass: "burgerking" },
]

const cities = [
  "Santa Fe",
  "Córdoba",
  "Buenos Aires",
  "Bariloche",
  "Entre Rios",
  "Carlos Paz",
  "Rosario",
  "Salta",
  "Esperanza",
  "Paraná",
  "Misiones",
]

const categories = [
  { name: "Hamburguesas", image: "/hamburguesa.jpg" },
  { name: "Sushi", image: "/sushi.jpg" },
  { name: "Helado",image: "/helado.jpg" },
  { name: "Pizza", image: "/pizza.jpg" },
  { name: "Meriendas", image: "/merienda.jpg" },
]

/* ===================== MAIN PAGE COMPONENT ===================== */
export default function HomePage() {
  return (
    <div className={styles.pageWrapper}>
      {/* ========== HEADER ========== */}
      <header className={styles.header}>
        <div className={`${styles.container} ${styles.headerInner}`}>
          <Link href="/" className={styles.logo}>
            <Image src="/logo.png" alt="PediloYa Logo" width={50} height={60} className={styles.logo} priority />
            <span className={styles.logoText}>PediloYa</span>
          </Link>
          <nav className={styles.nav}>
            <Link href="/login" className={styles.navButtonPrimary}>
              Iniciar Sesión
            </Link>
            <Link href="/registro" className={styles.navButtonSecondary}>
              Registrate
            </Link>
          </nav>
        </div>
      </header>

      {/* ========== HERO SECTION - FIXED ========== */}
      <section className={`${styles.container} ${styles.hero}`}>
        <div className={styles.heroInner}>
          {/* Left Column - Text Content */}
          <div className={styles.heroContent}>
            <h1 className={styles.heroTitle}>
              Tu comida favorita,
              <br />
              <span className={styles.heroTitleAccent}>siempre a tu alcance</span>
            </h1>
            <p className={styles.heroDescription}>
              Pide desde los mejores restaurantes, bares y comercios de Argentina. Entrega rápida, segura y con los
              mejores precios.
            </p>
            <Link href="/registro" className={styles.heroButton}>
              Registrate
            </Link>
          </div>

          {/* Right Column - Image with decorations */}
          <div className={styles.heroImageWrapper}>
            {/* Yellow clock icon */}
            <div className={`${styles.heroDecor} ${styles.heroDecorClock} ${styles.float}`}>
              <svg width="24" height="24" viewBox="0 0 24 24" fill="white">
                <path d="M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z" />
              </svg>
            </div>

            {/* Hero image placeholder */}
            <div className={styles.heroImage}>
              <div className={styles.heroImagePlaceholder}>
                <Image src="/plato-principal.jpeg" alt="Imagen de un plato principal" width={1500} height={1500} priority />
              </div>
            </div>

            {/* Yellow dashed curved arrow */}
            <div className={`${styles.heroDecor} ${styles.heroDecorArrow}`}>
              <svg width="50" height="60" viewBox="0 10 50 20" fill="none">
                <path
                  d="M5 45C5 25 20 10 40 15M40 15L32 8M40 15L32 22"
                  stroke="#e5a832"
                  strokeWidth="2.5"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeDasharray="5 4"
                />
              </svg>
            </div>

            {/* Coral dots */}
            <div className={`${styles.heroDecor} ${styles.heroDecorDot1}`}>
              <span className={styles.heroDotPink}></span>
            </div>
            <div className={`${styles.heroDecor} ${styles.heroDecorDot2}`}>
              <span className={styles.heroDotPink}></span>
            </div>
          </div>
        </div>
      </section>

      <div className={styles.sectionDivider}></div>

      {/* ========== SERVICES SECTION ========== */}
      <section className={`${styles.section} ${styles.services}`}>
        <div className={styles.container}>
          <ScrollReveal>
            <p className={styles.sectionLabel}>SERVICIOS</p>
            <h2 className={styles.sectionTitle}>
              Tu app favorita de
              <br />
              pedidos de comida
            </h2>
          </ScrollReveal>

          <div className={styles.servicesGrid}>
            <ScrollReveal delay={1}>
              <div className={styles.serviceCard}>
                <div className={styles.serviceIllustration}>
                  <Image src="/facil-de-pedir.jpeg" alt="Imagen" width={1500} height={1500} priority />
                </div>
                <h3 className={styles.serviceTitle}>Fácil de pedir</h3>
                <p className={styles.serviceDescription}>Para pedir comida solo necesitas unos pocos pasos</p>
              </div>
            </ScrollReveal>

            <ScrollReveal delay={2}>
              <div className={styles.serviceCard}>
                <div className={styles.serviceIllustration}>
                  <Image src="/entregas-rapidas.jpeg" alt="Imagen" width={120} height={120} priority />
                </div>
                <h3 className={styles.serviceTitle}>Entregas rápidas</h3>
                <p className={styles.serviceDescription}>Entrega siempre a tiempo, incluso más rápida.</p>
              </div>
            </ScrollReveal>

            <ScrollReveal delay={3}>
              <div className={styles.serviceCard}>
                <div className={styles.serviceIllustration}>
                  <Image src="/la-mejor-calidad.jpeg" alt="Imagen" width={120} height={120} priority />
                </div>
                <h3 className={styles.serviceTitle}>La mejor calidad</h3>
                <p className={styles.serviceDescription}>
                  No solo es rápido, para nosotros la calidad también es lo número uno.
                </p>
              </div>
            </ScrollReveal>
          </div>
        </div>
      </section>

      <div className={styles.sectionDivider}></div>

      {/* ========== RESTAURANTS SECTION ========== */}
      <section className={`${styles.section} ${styles.restaurants}`}>
        <ScrollReveal>
          <p className={styles.sectionLabel}>Locales Populares</p>
        </ScrollReveal>

        <Marquee className={styles.marqueeWrapper}>
          {restaurants.map((restaurant, index) => (
            <div key={`${restaurant.name}-${index}`} className={styles.restaurantCard}>
              <div className={styles.restaurantLogoWrapper}>
                <Image 
                  src={restaurant.logo} 
                  alt={restaurant.name}
                  width={150} 
                  height={150}
                  className={styles.restaurantLogoImage}
                />
              </div>
              <div className={styles.restaurantName}>{restaurant.name}</div>
            </div>
          ))}
        </Marquee>
      </section>

      <div className={styles.sectionDivider}></div>

      {/* ========== LOCATIONS SECTION ========== */}
      <section className={`${styles.section} ${styles.locations}`}>
        <div className={styles.container}>
          <ScrollReveal>
            <p className={styles.sectionLabel}>Lugares</p>
            <h2 className={styles.sectionTitle}>
              Realizamos entregas
              <br />
              en toda Argentina
            </h2>
          </ScrollReveal>

          <ScrollReveal delay={1}>
            <div className={styles.locationsGrid}>
              {cities.map((city) => (
                <span key={city} className={styles.locationPill}>
                  {city}
                </span>
              ))}
            </div>
            <p className={styles.locationsMore}>y más...</p>
          </ScrollReveal>
        </div>
      </section>

      <div className={styles.sectionDivider}></div>

      {/* ========== CATEGORIES SECTION ========== */}
      <section className={`${styles.section} ${styles.categories}`}>
        <ScrollReveal>
          <p className={styles.sectionLabel}>Destacados del Momento</p>
        </ScrollReveal>

        <Marquee reverse className={styles.categoriesMarquee}>
          {categories.map((category, index) => (
            <div key={`${category.name}-${index}`} className={styles.categoryCard}>
              <div className={styles.categoryImageWrapper}>
                <Image 
                  src={category.image} 
                  alt={category.name}
                  width={160}
                  height={120}
                  className={styles.categoryImage}
                />
              </div>
              <div className={styles.categoryName}>{category.name}</div>
            </div>
          ))}
        </Marquee>
      </section>

      <div className={styles.sectionDivider}></div>

      {/* ========== CTA SECTION ========== */}
      <section className={`${styles.section} ${styles.cta}`}>
        <div className={`${styles.container} ${styles.ctaInner}`}>
          <div className={`${styles.ctaStarburst} ${styles.ctaStarburstLeft}`}>
            <svg viewBox="0 0 100 100" fill="#ff4b7e" className={styles.spin}>
              <path d="M50 0 L55 40 L100 50 L55 60 L50 100 L45 60 L0 50 L45 40 Z" />
            </svg>
          </div>

          <div className={`${styles.ctaDecor} ${styles.ctaDecorLeft} ${styles.float}`}>
            <div className={styles.ctaImagePlaceholder}>🍣</div>
          </div>

          <ScrollReveal direction="scale">
            <h2 className={styles.ctaTitle}>
              Pedí tu comida favorita
              <br />
              mientras te quedás en casa
            </h2>
            <Link href="/registro" className={styles.ctaButton}>
              Empezá ahora →
            </Link>
          </ScrollReveal>

          <div className={`${styles.ctaDecor} ${styles.ctaDecorRight} ${styles.floatDelay}`}>
            <div className={styles.ctaImagePlaceholder}>🍱</div>
          </div>

          <div className={`${styles.ctaStarburst} ${styles.ctaStarburstRight}`}>
            <svg viewBox="0 0 100 100" fill="#ff8e4f" className={styles.spin}>
              <path d="M50 0 L55 40 L100 50 L55 60 L50 100 L45 60 L0 50 L45 40 Z" />
            </svg>
          </div>
        </div>
      </section>

      {/* ========== FOOTER ========== */}
      <footer className={styles.footer}>
        <div className={`${styles.container} ${styles.footerInner}`}>
          <p className={styles.footerText}>PediloYa © 2026. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  )
}

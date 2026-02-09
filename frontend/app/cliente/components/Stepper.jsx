import styles from './Stepper.module.css';

const steps = [
  { label: 'Productos', num: '01' },
  { label: 'Direccion', num: '02' },
  { label: 'Datos', num: '03' },
  { label: 'Medio de pago', num: '04' },
  { label: 'Gracias!', num: '05' },
];

export default function Stepper({ activeStep = 1 }) {
  return (
    <div className={styles.stepper}>
      {steps.map((step, i) => {
        const stepNum = i + 1;
        const isCompleted = stepNum < activeStep;
        const isActive = stepNum === activeStep;

        return (
          <div key={step.num} className={styles.stepItem}>
            {i > 0 && (
              <div className={`${styles.line} ${isCompleted || isActive ? styles.lineActive : ''}`} />
            )}
            <div className={styles.stepContent}>
              <div
                className={`${styles.circle} ${isCompleted ? styles.circleCompleted : ''} ${isActive ? styles.circleActive : ''}`}
              >
                {isCompleted ? (
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="3">
                    <polyline points="20 6 9 17 4 12" />
                  </svg>
                ) : (
                  <span>{step.num}</span>
                )}
              </div>
              <span className={`${styles.label} ${isActive ? styles.labelActive : ''} ${isCompleted ? styles.labelCompleted : ''}`}>
                {step.label}
              </span>
            </div>
          </div>
        );
      })}
    </div>
  );
}

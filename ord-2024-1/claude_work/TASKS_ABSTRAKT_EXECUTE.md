# Øvingsoppgaver: Abstrakt metodekontract + tilgangsmodifikator

Adresserer gap 1 og gap 2 fra ord-2024-1: `WeldChassisStep` manglet `public` og satte ikke `startTime`/`endTime`.

---

## Kontekst

Et jernbanenett har en rekke vedlikeholdstrinn (maintenance steps) som utføres i rekkefølge på togstrekninger.
Hvert trinn er representert av den abstrakte klassen `MaintenanceStep`. Tidsstempling er kritisk for statistikk.

```java
import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.Optional;

public abstract class MaintenanceStep {
    private String stepId;
    private boolean isAutomated;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;

    public MaintenanceStep(String stepId, boolean isAutomated) {
        this.stepId = stepId;
        this.isAutomated = isAutomated;
    }

    public String getStepId() { return stepId; }
    public boolean isAutomated() { return isAutomated; }

    public Optional<Duration> getStepDuration() {
        if (startTime == null || endTime == null) return Optional.empty();
        return Optional.of(Duration.between(startTime, endTime));
    }

    /**
     * Executes this maintenance step.
     *
     * Behavioural requirements:
     * - startTime must be set to ZonedDateTime.now() before work begins
     * - endTime must be set to ZonedDateTime.now() after work is done
     * - getStepDuration() must return at least 50ms after this method returns
     * - This method must be callable from outside the package
     */
    public abstract void execute();

    public void simulateWork() {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
    }
}
```

---

## Oppgave 1: TrackInspectionStep

Implementer klassen `TrackInspectionStep`:

```java
/**
 * Represents a track inspection step in a maintenance sequence.
 *
 * Behavioural requirements:
 * - Must extend {@link MaintenanceStep}
 * - Must be instantiatable from outside its defined package
 * - Constructor takes one parameter: stepId of type String
 * - isAutomated() must return false (manual step)
 * - execute() must fulfil the contract defined in MaintenanceStep:
 *   startTime and endTime must be set, duration at least 50ms
 */
// TODO: Implement TrackInspectionStep here
```

### Tester (skal bli grønne)

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

class TrackInspectionStepTest {

    @Test
    void step_can_be_instantiated_with_stepId() {
        TrackInspectionStep step = new TrackInspectionStep("TI-01");
        assertNotNull(step);
        assertEquals("TI-01", step.getStepId());
    }

    @Test
    void step_is_not_automated() {
        TrackInspectionStep step = new TrackInspectionStep("TI-02");
        assertFalse(step.isAutomated());
    }

    @Test
    void execute_sets_startTime_and_endTime() {
        TrackInspectionStep step = new TrackInspectionStep("TI-03");
        assertFalse(step.getStepDuration().isPresent()); // ikke satt ennå
        step.execute();
        assertTrue(step.getStepDuration().isPresent());  // skal nå være satt
    }

    @Test
    void execute_duration_is_at_least_50ms() {
        TrackInspectionStep step = new TrackInspectionStep("TI-04");
        step.execute();
        Duration duration = step.getStepDuration().get();
        assertTrue(duration.toMillis() >= 50, "Duration should be at least 50ms");
    }

    @Test
    void startTime_is_before_endTime() {
        TrackInspectionStep step = new TrackInspectionStep("TI-05");
        step.execute();
        assertTrue(step.startTime.isBefore(step.endTime));
    }
}
```

### Fasit

```java
import java.time.ZonedDateTime;

public class TrackInspectionStep extends MaintenanceStep {

    public TrackInspectionStep(String stepId) {
        super(stepId, false); // false = ikke automatisert
    }

    @Override
    public void execute() {
        this.startTime = ZonedDateTime.now();  // sett startTime FØRST
        super.simulateWork();                  // simuler arbeid (50ms)
        this.endTime = ZonedDateTime.now();    // sett endTime ETTER
    }
}
```

### Advarsel — fellene i denne oppgaven

**Felle 1**: Å glemme `public` på klassedeklarasjonen. Default er package-private. JavaDoc sier "instantiatable from outside its defined package" — dette krever alltid `public`.

**Felle 2**: Å bare kalle `super.simulateWork()` i execute() uten å sette `startTime`/`endTime`. `getStepDuration()` returnerer da `Optional.empty()`, og alle statistikk-metoder gir feil svar.

**Sjekkliste ved override av abstrakt metode:**
1. Les superklassens JavaDoc for metoden linje for linje
2. Identifiser alle sideeffekter ("must set X", "must notify Y")
3. Implementer hvert punkt
4. Sjekk tilgangsmodifikator på klassen (`public`?)

---

## Oppgave 2 (Vanskeligere): SignalCalibrationStep

```java
/**
 * Represents an automated signal calibration step.
 *
 * Behavioural requirements:
 * - Must extend MaintenanceStep
 * - Must be instantiatable from outside its defined package
 * - Constructor: stepId (String) and calibrationTarget (double) — target value in Hz
 * - isAutomated() must return true
 * - execute() fulfils MaintenanceStep contract (startTime, endTime, ≥50ms)
 * - getCalibrationTarget() returns the calibration target
 * - If calibrationTarget <= 0 in the constructor, throw IllegalArgumentException
 */
// TODO: Implement SignalCalibrationStep here
```

### Tester

```java
class SignalCalibrationStepTest {

    @Test
    void constructor_throws_for_nonpositive_target() {
        assertThrows(IllegalArgumentException.class, () -> new SignalCalibrationStep("SC-01", 0.0));
        assertThrows(IllegalArgumentException.class, () -> new SignalCalibrationStep("SC-02", -5.0));
    }

    @Test
    void getCalibrationTarget_returns_correct_value() {
        SignalCalibrationStep step = new SignalCalibrationStep("SC-03", 433.5);
        assertEquals(433.5, step.getCalibrationTarget());
    }

    @Test
    void step_is_automated() {
        SignalCalibrationStep step = new SignalCalibrationStep("SC-04", 100.0);
        assertTrue(step.isAutomated());
    }

    @Test
    void execute_sets_times_and_meets_duration() {
        SignalCalibrationStep step = new SignalCalibrationStep("SC-05", 100.0);
        step.execute();
        assertTrue(step.getStepDuration().isPresent());
        assertTrue(step.getStepDuration().get().toMillis() >= 50);
    }
}
```

### Fasit

```java
import java.time.ZonedDateTime;

public class SignalCalibrationStep extends MaintenanceStep {
    private double calibrationTarget;

    public SignalCalibrationStep(String stepId, double calibrationTarget) {
        super(stepId, true); // true = automatisert
        if (calibrationTarget <= 0) throw new IllegalArgumentException("Target must be positive");
        this.calibrationTarget = calibrationTarget;
    }

    public double getCalibrationTarget() {
        return calibrationTarget;
    }

    @Override
    public void execute() {
        this.startTime = ZonedDateTime.now();
        super.simulateWork();
        this.endTime = ZonedDateTime.now();
    }
}
```

### Advarsel — tilleggsfelle

Konstruktørvalidering: legg alltid `if (ugyldig) throw` ETTER `super(...)`. Du kan ikke kaste unntak FØR `super()` kalles — det gir kompileringsfeil.

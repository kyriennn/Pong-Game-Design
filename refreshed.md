# Java Pong — Reference Notes

Built with Java + Swing. Classes: `Main`, `GameFrame`, `GamePanel`, `GameObject`, `Paddle`, `Ball`, `Score`.

---

## 1. The Big Picture

### Who creates what
```
Main
 └─ GameFrame (the window)
     └─ GamePanel (the game: loop, input, collisions, drawing)
         ├─ Paddle (left)
         ├─ Paddle (right)
         ├─ Ball
         └─ Score
```

### What runs when
- **Once, at startup:** `Main` → `GameFrame` constructor → `GamePanel` constructor (creates objects, registers key listener, starts the `Timer`)
- **Every tick (~60×/sec):** the `Timer` calls `gameLoop()`:
  - `if (gameOver)` → repaint and `return` early
  - `updateDirections()` → turn held keys into paddle directions
  - `leftPaddle.update()`, `rightPaddle.update()`, `ball.update()` → move everything
  - `checkCollisions()` → ball vs paddles
  - `checkScore()` → ball out of bounds?
  - `repaint()` → asks Swing to redraw
- **Whenever Swing redraws:** `paintComponent(g)` → clear, draw net, paddles, ball, score, game-over text
- **Whenever a key goes down/up:** `keyPressed` / `keyReleased` → set flags only (no movement here)

### Core idea: update → check → draw
- Game logic lives in `gameLoop()`
- Drawing lives in `paintComponent()`
- Never mix them — `paintComponent` should only draw

---

## 2. Class Responsibilities

**Rule of thumb: the class that owns the data is the one that changes it.**

### Main
- Only starts the program: `SwingUtilities.invokeLater(() -> new GameFrame());`

### GameFrame (extends JFrame)
- Sets up the window, adds the panel, gives it focus
- No game logic — if it grows past ~10 lines, something leaked in

### GamePanel (extends JPanel, implements KeyListener)
- The coordinator: owns the objects, the timer, key flags, and `gameOver`
- Decides **when** things happen (collisions, scoring, game over)
- Tells objects to update/draw — doesn't move them itself

### GameObject (abstract)
- Shared state: `x`, `y`, `speed`, `width`, `height` (`protected`)
- Shared behaviour: `getBounds()`, getters
- Forces subclasses to implement `update()` and `draw(Graphics g)`

### Paddle (extends GameObject)
- Knows how to move itself and stay inside its own half (`minX` / `maxX`)
- Gets its direction from `GamePanel` via `setDirection(x, y)`

### Ball (extends GameObject)
- Knows how to move, bounce off top/bottom walls, reset, and react to a hit
- Does **not** handle left/right edges — that's scoring, so `GamePanel` does it

### Score (standalone class — not a GameObject)
- Holds the points and the win **rule** (`hasWinner()`)
- `GamePanel` decides what to **do** when someone wins
- Not a GameObject because it doesn't move or collide — no real "is-a" relationship

---

## 3. Imports Cheat Sheet

Don't memorise these — know which **package** to look in. VSCode's `Ctrl+.` on a red squiggle can add them.

### javax.swing — windows and UI
- `JFrame` — the window
- `JPanel` — drawing surface inside the window
- `Timer` — fires an event every N ms (the game loop). **Not** `java.util.Timer`
- `SwingUtilities` — `invokeLater()` to start the GUI on the right thread

### java.awt — drawing and shapes
- `Graphics` — the "pen" passed into `paintComponent` and `draw`
- `Color` — `Color.WHITE`, `Color.BLACK`, etc.
- `Font` — text style and size
- `FontMetrics` — measures text (for centering)
- `Rectangle` — box with `intersects()` for collisions
- `Dimension` — width + height pair for `setPreferredSize`

### java.awt.event — input
- `KeyListener` — interface: `keyPressed`, `keyReleased`, `keyTyped`
- `KeyEvent` — key codes like `KeyEvent.VK_W`, `VK_UP`, `VK_SPACE`

### java.util — general utilities
- `Random` — random numbers (ball serve direction)

### Imports are per file
- Each file imports only what **it** uses
- `Ball` needs `Graphics`, `Color`, `Random`; `GameFrame` only needs `JFrame`

---

## 4. The "Weird" Methods — Explained in Context

### Constructors and inheritance
- **`super(...)`** — calls the parent class's constructor. Must be the first line. `Paddle` uses it to pass `x, y, speed, width, height` up to `GameObject`; `GameFrame` uses `super("Pong")` to set the title
- **`this.x = x`** — `this.x` is the field, `x` is the parameter. Only needed when names clash
- **Constructors can do setup, not just assign fields** — `GameFrame`'s constructor calls `setTitle`, `pack`, etc. Those are inherited `JFrame` methods, called on itself (an invisible `this.`)
- **Declare parameters once, pass values as often as you like** — `Ball(double size)` → `super(x, y, speed, size, size)`

### Annotations and abstract methods
- **`@Override`** — optional, but the compiler errors if the method doesn't actually override anything (catches typos like `paintComponet`)
- **`abstract void update();`** — no body; every subclass must write its own
- **`keyTyped` left empty** — required by `KeyListener`, not used. Add a comment saying why

### Swing setup
- **`setPreferredSize(new Dimension(WIDTH, HEIGHT))`** — tells `pack()` how big the panel wants to be. Without it the window shrinks to nothing
- **`pack()`** — sizes the window around its contents. Must come **after** `add(...)`
- **`setLocationRelativeTo(null)`** — centers the window. Must come **after** `pack()`
- **`setVisible(true)`** — last, so the user never sees the window resize
- **`setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)`** — otherwise X only hides the window
- **`SwingUtilities.invokeLater(...)`** — runs GUI code on the Event Dispatch Thread (EDT). Swing isn't thread-safe, so all GUI work belongs on that one thread

### The game loop
- **`new Timer(DELAY, e -> gameLoop())`** — every `DELAY` ms, run `gameLoop()`. `e -> ...` is a **lambda** (a short way to pass "what to run"); `e` is the timer event, unused
- **`gameLoop()` is its own method** — not inside the constructor. Java doesn't allow methods inside methods
- **`repaint()`** — doesn't draw directly; it asks Swing to call `paintComponent` soon
- **`super.paintComponent(g)`** — clears the previous frame. Without it you get trails

### Input
- **`addKeyListener(this)`** — "when this panel gets key events, call the key methods on me". Works because `GamePanel implements KeyListener`
- **`setFocusable(true)` + `panel.requestFocusInWindow()`** — key events only go to the focused component. Request focus **after** `setVisible(true)`, on the **same** panel you added
- **Why flags instead of moving in `keyPressed`** — flags remember every held key. Setting direction directly breaks when keys overlap (hold W, tap S, release S → paddle stops even though W is held)

### Collisions
- **`getBounds()`** — written by you in `GameObject`, returns a `Rectangle` at the object's current position
- **`intersects()`** — built into `java.awt.Rectangle`; true if two boxes overlap
- **`ball.getBounds().intersects(paddle.getBounds())`** — method chaining: call a method on the result of another
- **`ball.getDx() <= 0 && ...`** — only count a hit if the ball is moving **toward** that paddle, so one hit isn't counted every tick while they overlap. `&&` stops at the first false, so the cheap check goes first

### Text
- **`drawString(text, x, y)`** — `y` is the **baseline** (bottom of the letters), not the top
- **`FontMetrics.stringWidth(text)`** — measure after `setFont`, then subtract half to center

---

## 5. Reusable Tricks

### Direction × speed
```java
x += xDirection * speed;   // -1, 0, or 1 → left, still, right — no if-statements
```

### Keys → direction
```java
int y = (sPressed ? 1 : 0) - (wPressed ? 1 : 0);   // both or neither held = 0
```
- `condition ? a : b` is the **ternary operator**

### Center something = subtract half its size
```java
x = centerX - width / 2;   // x, y are the TOP-LEFT corner, not the center
```

### One formula for both sides
```java
x = WIDTH / 2.0 + side * WIDTH / 4.0 - width / 2;   // side -1 → left quarter, 1 → right quarter
```

### Clamp after moving
```java
x += dx;
if (x < minX) x = minX;
if (x > maxX) x = maxX;
// or: x = Math.max(minX, Math.min(x, maxX));
```

### Set direction, don't flip it
```java
dy = Math.abs(dy);    // top wall: must go DOWN
dy = -Math.abs(dy);   // bottom wall: must go UP
dx = direction * speed;   // paddle hit: always away from that paddle
```
- Flipping (`dy = -dy`) breaks if the check runs twice or the ball starts still

### Hit angle from where the ball hits the paddle
```java
double offset = (ballCenter - paddleCenter) / (paddleHeight / 2);   // -1 top, 0 middle, 1 bottom
offset = Math.max(-1, Math.min(offset, 1));                          // clamp corner hits
dy = offset * speed;
```

### Early-return guard
```java
if (side == 0) {
    return;   // must come BEFORE the code it's meant to skip
}
```

### Overloading to avoid duplicate code
```java
public void reset() { reset(RANDOM.nextBoolean() ? -1 : 1); }   // game start
public void reset(int side) { /* the real logic */ }            // after a point
```

---

## 6. OOP Concepts Used

- **Inheritance** — `Paddle` and `Ball` extend `GameObject`; `GameFrame` extends `JFrame`; `GamePanel` extends `JPanel`
- **Abstract class** — `GameObject` can't be created on its own; it defines what every game object must do
- **Polymorphism** — `GamePanel` can call `update()` / `draw()` on any `GameObject` without caring which kind it is
- **Encapsulation** — fields are `private`/`protected`; other classes use methods (`addScore`, `setDirection`, getters) instead of editing fields directly
- **Interfaces** — `implements KeyListener` is a promise to provide three methods
- **Method overloading** — `reset()` and `reset(int side)`
- **Composition** — `GamePanel` *has* paddles, a ball and a score (has-a, not is-a)
- **Same code, different data** — one `Paddle.update()` serves both paddles because each stores its own `minX` / `maxX`

### When to use constructor parameters
- Only for values that **differ between objects at creation** (paddle position, limits)
- Not for values that are always the same (use constants) or set later at runtime (`direction`)
- Not for values the constructor ignores (`Ball` doesn't need `x, y` — `reset()` sets them)
- More than ~4–5 parameters → rethink, or at least one per line with comments

---

## 7. Bugs I Hit (and Fixes)

- **"The public type X must be defined in its own file"** — file name must match the public class name exactly, including case
- **"Compact Source Files … source level 25"** — `main` was written outside a class. Wrap it in `public class Main { }`
- **"cd: too many arguments"** — VSCode's Code Runner built a Windows command for a Git Bash terminal; spaces in the folder name broke it. Use `javac *.java` then `java Main`, or the "Run | Debug" link above `main`
- **Window with only a title bar** — panel had no preferred size (or tiny `WIDTH`/`HEIGHT`)
- **Keys did nothing** — panel didn't have focus; fixed with `requestFocusInWindow()` after `setVisible(true)`
- **"Field not used" warning on `score`** — assigning a field isn't using it; the warning goes once something reads it
- **Ball stuck sliding along the bottom wall** — two `if`s both flipped `dy`, cancelling out. One check per wall, clamp + set direction once
- **Ball never launched** — debug prints showed `dx` going 12 → 0 every tick. `checkScore()` had its `if (side == 0) return;` at the **bottom**, so `ball.reset(0)` ran every tick. Also used `getDx()` where `getX()` was meant
- **Running Java 8** — VSCode was using `jre-1.8`. Install JDK 21 and select it with "Java: Configure Java Runtime"

### Debugging approach that worked
- Test each stage before moving on (window → loop → input → collisions → scoring)
- Temporary tests like `leftPaddle.setDirection(0, 1);` to check one piece in isolation
- `System.out.println` at key points to see **where** values go wrong, then find who changes them
- Read the terminal for red exception text — Swing keeps the window open even when the loop is crashing every tick

---

## 8. Git Commands Used

```bash
git clone <url>                 # copy a GitHub repo locally
git status                      # what changed?
git add .                       # stage everything
git commit -m "message"         # save a snapshot
git push                        # upload to GitHub
git log --oneline -5            # recent commits
git mv oldName.java NewName.java   # case-only renames on Windows
```
- **"nothing to commit, working tree clean"** — no changes on disk: unsaved files, already committed, or editing files outside the repo folder
- Commit after each working stage so you can roll back
- Add a `.gitignore` for `*.class`, `out/`, `bin/`, `.idea/`, `.vscode/`
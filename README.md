# Neurysteria

> *“Ever seen a neural network throw a tantrum? Meet **Neurysteria**, the AI that gets emotional. - A neural net which, the more it fails, the more it fumes. Beware — it learns... in wrath.” *

---

## 🧭 Table of Contents

- [Overview](#overview)
- [Emotional States & Behaviors](#emotional-states--behaviors)
    - [CALM](#calm)
    - [ANGRY](#angry)
    - [HYSTERICAL](#hysterical)
    - [OBSESSED](#obsessed)
- [The Rage Mechanism](#the-rage-mechanism)
- [What Can This Moody Network Do?](#what-can-this-moody-network-do)
- [Architecture & Code Design](#architecture--code-design)
- [Training Tips](#training-tips)
- [Conclusion](#conclusion)

---

## Overview

**Neurysteria** is an experimental neural network that doesn’t just learn – it *feels*. Designed to simulate frustration and emotion, it includes distinct emotional states that influence its behavior: **CALM**, **ANGRY**, **HYSTERICAL**, and **OBSESSED**. Each state changes how the network learns, reacts to tasks, and even rewires its own architecture.

Instead of quietly adjusting weights like a good little AI, Neurysteria might:
- Flip out and restructure its topology (HYSTERICAL)
- Get stuck repeating a failing strategy (OBSESSED)
- Or angrily switch to a new solving approach like a tilted poker player (ANGRY)

Despite all this drama, it **can solve non-trivial problems** defined in the `task` package — though never without a few emotional breakdowns.

---

## Emotional States & Behaviors

Neurysteria has four distinct modes of operation, each triggered by its internal *rage meter* based on past failure:

### CALM

> *“I got this.”*

In this default state, the network behaves rationally and predictably. It applies standard strategies to solve problems and updates its parameters carefully. Learning is steady and stable.

### ANGRY

> *“I’ve had ENOUGH of this strategy!”*

Triggered by repeated failures. The network becomes emotionally unstable and starts switching strategies rapidly — trying alternative approaches even if they’re risky. Like a frustrated human, it experiments wildly in hopes of a breakthrough.

### HYSTERICAL

> *“Tear it all down and start over!”*

The most unstable state. Neurysteria randomly mutates its architecture: deleting neurons, adding new ones, rewiring connections. Chaos reigns. It might solve the task — or destroy itself in the process.

### OBSESSED

> *“This will work. This WILL work. This... will... work...”*

The network becomes stuck on a single failing idea and loops endlessly, ignoring feedback. Even bad results won’t deter it. Escape requires external intervention or enough time to trigger a rage spike.

---

## The Rage Mechanism

Neurysteria tracks **internal frustration**. Each time it fails to solve a task, its *rage counter* increases.

- Once rage exceeds a threshold → it transitions from CALM to ANGRY.
- If failure continues → it may go HYSTERICAL or become OBSESSED.
- Success reduces rage and may return it to CALM.

This mechanism makes training **challenging**:
- Anger changes learning strategy
- Hysteria alters the network architecture
- Obsession causes loops
- You never know how it will react next

It’s part machine, part emotional teenager.

---

## What Can This Moody Network Do?

Despite its instability, Neurysteria is capable of solving real problems.

In the [`task`](./src/main/java/io/github/swampus/neurysteria/domain/task) package, you’ll find non-trivial computational challenges:
- Pattern matching
- Decision-making
- Search problems

Sometimes it solves them calmly. Other times it spirals into madness, rewires itself, and *still* stumbles onto the answer.

Each run is unpredictable — and that’s the point.

---

## Architecture & Code Design

Neurysteria is written in **Java 17** and structured using **Clean Architecture**:

## Training Tips

- **Start simple.** Let it win early — builds confidence.
- **Watch rage levels.** Spiking too fast? Adjust thresholds.
- **Customize strategies.** Different learning logic for each state.
- **Don’t expect determinism.** Each training run is a new emotional journey.
- **Reward success.** Positive feedback helps stabilize behavior.
- **Pray it doesn’t get OBSESSED.** Because it *will* ignore you.

---

## 🧪 Getting Started

To run Neurysteria locally, execute the following main class:

```bash
io.github.swampus.neurysteria.infrastructure.MainApplication
```

This launches the network with a sample task and lets you witness how calm reasoning devolves into algorithmic rage, chaos, and possibly… success.

Make sure you're using Java 17+ and a proper Maven-aware IDE.

        TaskType type = TaskType.CHAIN_CALCULATOR;  // switch Task

        Task task = TaskFactory.create(type);

        for (int i = 0; i < 100; i++) { //increase counter, give a chance

---

## Conclusion

Neurysteria is not just another neural network — it’s an **emotionally unstable AI experiment**.

It rages.  
It panics.  
It rewires its own brain.  
And sometimes, against all odds, it solves the problem anyway.

If you’re tired of sterile, silent machine learning models and want to explore AI with personality, unpredictability, and charm — **welcome to Neurysteria**.

---

MIT License • Created by [@swampus](https://github.com/swampus) •  
Pull requests welcome (unless they upset the network 😉)
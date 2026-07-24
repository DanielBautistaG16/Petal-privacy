# PETAL ROUTES & PLACES MODULE
## Product Design Specification

**Version:** 1.0
**Date:** January 2026
**Author:** Product, UX & AI Architecture Team
**Status:** Ready for Implementation

---

## EXECUTIVE SUMMARY

Petal Routes & Places is an intelligent cycling discovery module that acts as a **quiet, premium concierge** for urban cyclists. Unlike Google Maps or tourist apps, this module makes decisions FOR the user, reducing choice fatigue and prioritizing cycling comfort, safety, and pleasure.

**Core Principle:**
*"Less choice. Better decision."*

**Key Differentiator:**
Voice-first, AI-curated, cycling-optimized recommendations that feel human, not algorithmic.

---

## 1. PRODUCT VISION

### Goal
Transform urban cycling from navigation into **intelligent discovery**:
- Reduce decision fatigue through curated recommendations
- Prioritize comfort, safety, and cycling pleasure over speed
- Create moments of delight through contextual, timely suggestions
- Work seamlessly with hands-free voice interaction

### What This Is NOT
- ❌ Not Google Maps (we don't show 20 route options)
- ❌ Not TripAdvisor (we don't list 100 restaurants)
- ❌ Not Strava (we don't gamify or compete)

### What This IS
- ✅ A discreet local concierge who knows cycling
- ✅ A calm, intelligent companion for urban exploration
- ✅ A premium mobility experience

### Success Metrics
- **Primary:** % of users who follow recommended routes without changes
- **Secondary:** Voice interaction success rate
- **Delight:** User testimonials mentioning "calm", "trust", "pleasant surprise"

---

## 2. USER TYPES & ADAPTIVE BEHAVIOR

### User Personas

#### 1. **The Commuter** (35% of users)
- **Profile:** Daily city cycling, time-conscious
- **Needs:** Safe routes, traffic avoidance, ETA accuracy
- **Petal Adaptation:** Prioritize speed + safety, minimize stops

#### 2. **The Explorer** (30% of users)
- **Profile:** Visitors, tourists, weekend riders
- **Needs:** Discovery, culture, scenic routes
- **Petal Adaptation:** Maximize scenic value, cultural stops, leisurely pace

#### 3. **The Local Casual** (25% of users)
- **Profile:** Occasional riders, errand running
- **Needs:** Simplicity, nearby recommendations
- **Petal Adaptation:** Short distances, familiar areas, practical stops

#### 4. **The Premium User** (10% of users)
- **Profile:** Petal e-bike owners, high engagement
- **Needs:** Exclusive recommendations, personalized experiences
- **Petal Adaptation:** Hidden gems, priority access, tailored routes

### Context-Aware Adaptation

The system automatically adapts based on:

| Context Factor | Low State | High State |
|----------------|-----------|------------|
| **Time of Day** | Morning: efficiency routes | Evening: leisure routes |
| **Battery Level** | <20%: short routes only | >80%: adventurous routes |
| **Weather** | Rain: covered paths | Sunny: scenic parks |
| **Traffic** | Rush hour: quiet streets | Off-peak: main routes OK |
| **User Speed** | Moving fast: no interruptions | Stopped: offer suggestions |
| **Location Type** | Residential: quiet discovery | Tourist area: avoid crowds |

---

## 3. UX FLOW – SCREEN BY SCREEN

### A. ENTRY POINT FROM HOME

**Purpose:** Initiate discovery mode without friction

**UI Layout:**
```
┌─────────────────────────────────┐
│ [Petal Logo]      [Profile]     │
│                                  │
│  Good afternoon, Elena           │
│  Current location                │
│  📍 El Carmen, Valencia          │
│                                  │
│  ┌──────────────────────────┐   │
│  │                          │   │
│  │  "Where would you        │   │
│  │   like to go?"           │   │
│  │                          │   │
│  │  [Explore nearby]        │   │
│  │  [Plan a route]          │   │
│  │  [Surprise me]           │   │
│  │                          │   │
│  └──────────────────────────┘   │
│                                  │
│  🎙️ Or just ask me              │
│                                  │
└─────────────────────────────────┘
```

**Copy (exact wording):**
- Greeting: "Good [morning/afternoon/evening], [Name]"
- Location: "📍 [Neighborhood], [City]"
- Primary CTA: "Where would you like to go?"
- Buttons:
  - "Explore nearby" (primary)
  - "Plan a route" (secondary)
  - "Surprise me" (tertiary)
- Voice prompt: "🎙️ Or just ask me"

**Button Hierarchy:**
1. **Explore nearby** – Petal Blue, primary button
2. **Plan a route** – Outline, secondary
3. **Surprise me** – Text link, tertiary

**User Feeling:**
"Calm, welcomed, no pressure to decide immediately"

**Voice Alternative:**
User can bypass this screen entirely by saying:
- "Petal, take me somewhere nice"
- "Petal, I'm hungry"
- "Petal, show me something beautiful"

---

### B. INTENT SELECTION SCREEN

**Purpose:** Understand user mood/intent without overwhelming options

**UI Layout:**
```
┌─────────────────────────────────┐
│ ← Back                          │
│                                  │
│  What are you in the mood for?  │
│                                  │
│  ┌────────┐  ┌────────┐         │
│  │  🍽️    │  │  🏛️    │         │
│  │  Eat   │  │Culture │         │
│  └────────┘  └────────┘         │
│                                  │
│  ┌────────┐  ┌────────┐         │
│  │  🌳    │  │  ☕    │         │
│  │ Relax  │  │ Coffee │         │
│  └────────┘  └────────┘         │
│                                  │
│  ┌────────┐  ┌────────┐         │
│  │  🎨    │  │  ✨    │         │
│  │  Art   │  │Surprise│         │
│  └────────┘  └────────┘         │
│                                  │
│  🎙️ "I'm looking for..."        │
│                                  │
└─────────────────────────────────┘
```

**Copy:**
- Title: "What are you in the mood for?"
- Intent cards (6 max):
  - 🍽️ "Eat" – Food & restaurants
  - 🏛️ "Culture" – Museums, architecture
  - 🌳 "Relax" – Parks, quiet spaces
  - ☕ "Coffee" – Cafés, breaks
  - 🎨 "Art" – Galleries, street art
  - ✨ "Surprise" – Petal decides

**Design:**
- Cards: 120×120px, white bg, soft shadow
- Icons: 32px, subtle color (not bright)
- Grid: 2 columns, 8px spacing

**User Feeling:**
"This is simple. I don't need to think hard."

**Voice Alternative:**
- "Petal, I want to eat"
- "Petal, show me something cultural"
- "Petal, I need a coffee break"

---

### C. RECOMMENDED ROUTE SCREEN (Main Experience)

**Purpose:** Present ONE curated route recommendation with confidence

**UI Layout:**
```
┌─────────────────────────────────┐
│ ← Back              [···Menu]   │
│                                  │
│  Recommended for you             │
│                                  │
│  ┌──────────────────────────┐   │
│  │ [Mini Map Preview]       │   │
│  │                          │   │
│  │ Your route               │   │
│  │ 4.2 km · 18 min · Easy   │   │
│  │                          │   │
│  └──────────────────────────┘   │
│                                  │
│  A quiet ride through El Carmen  │
│  with a stop at a hidden café    │
│                                  │
│  📍 Stops on the way (3)         │
│                                  │
│  ┌──────────────────────────┐   │
│  │ 🏛️ Plaza de la Virgen    │   │
│  │    Worth a 2-min stop    │   │
│  └──────────────────────────┘   │
│                                  │
│  ┌──────────────────────────┐   │
│  │ ☕ Café de las Horas      │   │
│  │    Perfect for a break   │   │
│  └──────────────────────────┘   │
│                                  │
│  ┌──────────────────────────┐   │
│  │ 🌳 Jardín del Turia       │   │
│  │    Shaded bike path      │   │
│  └──────────────────────────┘   │
│                                  │
│  [Start Navigation]              │
│  [See alternatives]              │
│                                  │
└─────────────────────────────────┘
```

**Copy (exact wording):**
- Header: "Recommended for you"
- Route title: Generated by AI (e.g., "A quiet ride through El Carmen")
- Route stats: "[Distance] km · [Time] min · [Difficulty]"
- Route description: 1 sentence, human tone
- Stops header: "📍 Stops on the way ([number])"
- Each stop:
  - Icon + Name
  - Short explanation (4–6 words max)

**Button Hierarchy:**
1. **Start Navigation** – Petal Blue, primary (60% width)
2. **See alternatives** – Outline, secondary (40% width)

**Design Details:**
- Map preview: 16:9 aspect ratio, subtle Petal branding
- Stop cards: White bg, 4dp elevation, 12dp corner radius
- Typography: Route title 20sp bold, description 14sp regular
- Padding: 16dp all sides

**User Feeling:**
"I trust this. I don't need to overthink it."

**Voice Alternative:**
- "Petal, start the route"
- "Petal, tell me about the stops"
- "Petal, show me something else"

---

### D. ROUTE DETAILS & SMART STOPS

**Purpose:** Provide depth without clutter when user wants more info

**Trigger:** User taps "See alternatives" or a specific stop

**UI Layout:**
```
┌─────────────────────────────────┐
│ ← Back to route                 │
│                                  │
│  Route details                   │
│                                  │
│  ┌──────────────────────────┐   │
│  │ [Detailed Map]           │   │
│  │                          │   │
│  │ 🚴 Bike lanes: 85%       │   │
│  │ 📊 Elevation: Mostly flat│   │
│  │ 🚦 Traffic: Low          │   │
│  │                          │   │
│  └──────────────────────────┘   │
│                                  │
│  Why this route?                 │
│                                  │
│  Petal chose this for comfort    │
│  and safety. You'll ride mostly  │
│  on dedicated bike lanes through │
│  quiet neighborhoods.            │
│                                  │
│  Best time: Now is perfect       │
│  Weather: Sunny, 22°C            │
│                                  │
│  [Start Navigation]              │
│                                  │
└─────────────────────────────────┘
```

**Copy:**
- Title: "Route details"
- Stats:
  - "🚴 Bike lanes: [percentage]%"
  - "📊 Elevation: [Mostly flat / Some hills / Hilly]"
  - "🚦 Traffic: [Low / Medium / Busy]"
- Explanation header: "Why this route?"
- Explanation: 2–3 sentences, conversational
- Context: "Best time: [recommendation]" + "Weather: [current]"

**User Feeling:**
"Petal really thought about this. I feel safe."

---

### E. ALTERNATIVE: PLACE RECOMMENDATION (No Route)

**Purpose:** When user just wants a destination, not a full route

**Trigger:** User says "I just want a coffee" or taps "Explore nearby"

**UI Layout:**
```
┌─────────────────────────────────┐
│ ← Back                          │
│                                  │
│  Places nearby                   │
│                                  │
│  ┌──────────────────────────┐   │
│  │ ☕ Dulce de Leche         │   │
│  │    5 min away            │   │
│  │                          │   │
│  │    Cozy, not touristy.   │   │
│  │    Perfect for now.      │   │
│  │                          │   │
│  │    [Navigate]   [Tell me │   │
│  │                  more]   │   │
│  └──────────────────────────┘   │
│                                  │
│  ┌──────────────────────────┐   │
│  │ ☕ Café Negrito           │   │
│  │    8 min away            │   │
│  │                          │   │
│  │    Local favorite. Quiet │   │
│  │    terrace.              │   │
│  │                          │   │
│  │    [Navigate]   [Tell me │   │
│  │                  more]   │   │
│  └──────────────────────────┘   │
│                                  │
│  [Show more places]              │
│                                  │
└─────────────────────────────────┘
```

**Copy:**
- Title: "Places nearby"
- Each place card:
  - Icon + Name
  - "[Time] away" (cycling time)
  - 1–2 sentence description (human, not Yelp-like)
  - "Why now" explanation (contextual)

**Design Rules:**
- Max 3 places shown initially
- Cards: Full width, stacked vertically
- Each card: Icon (left), text (center), buttons (bottom)

**User Feeling:**
"These feel handpicked, not random search results."

---

### F. NAVIGATION START & VOICE INTERACTION

**Purpose:** Seamless transition to hands-free cycling mode

**UI During Navigation:**
```
┌─────────────────────────────────┐
│ [Full-screen Map]                │
│                                  │
│ ┌──────────────────────────┐   │
│ │ Next: Turn right         │   │
│ │ In 200 m                 │   │
│ └──────────────────────────┘   │
│                                  │
│                                  │
│ [Compact card at bottom]         │
│ ┌──────────────────────────┐   │
│ │ 🎙️ Listening...          │   │
│ │                          │   │
│ │ "Skip this stop"         │   │
│ │ "Tell me about this"     │   │
│ │ "Where's a restroom?"    │   │
│ └──────────────────────────┘   │
│                                  │
│ [End Navigation]                 │
│                                  │
└─────────────────────────────────┘
```

**Voice Interaction States:**

1. **Silent Mode** (default while cycling)
   - Petal doesn't interrupt
   - Shows visual cues only
   - User can activate voice anytime

2. **Active Listening** (when user speaks)
   - Screen shows "🎙️ Listening..."
   - Processes command
   - Responds via TTS (brief, calm)

3. **Proactive Suggestions** (when stopped >30 sec)
   - Gentle notification: "There's a nice café 2 minutes ahead"
   - User can ignore or engage

**Voice Commands During Navigation:**
- "Petal, where are we?" → Brief location update
- "Petal, skip this stop" → Removes stop, recalculates
- "Petal, tell me about this place" → 15-second description
- "Petal, I need a restroom" → Finds nearest, adds to route
- "Petal, I'm tired" → Suggests rest stop or shortcut home
- "Petal, pause navigation" → Stops voice, keeps map

**Voice Response Rules:**
- Max 15 seconds spoken
- Calm, reassuring tone
- No technical jargon
- Acknowledge, then act: "Got it. Skipping the museum."

**User Feeling:**
"I'm cycling, hands-free, and Petal just... gets it."

---

### G. END-OF-ROUTE ACTIONS

**Purpose:** Close the loop, learn preferences, encourage future use

**UI Layout:**
```
┌─────────────────────────────────┐
│                                  │
│  You've arrived                  │
│                                  │
│  ┌──────────────────────────┐   │
│  │                          │   │
│  │  Route completed         │   │
│  │  4.2 km · 19 min         │   │
│  │                          │   │
│  │  How was your ride?      │   │
│  │                          │   │
│  │  😊  🙂  😐             │   │
│  │                          │   │
│  └──────────────────────────┘   │
│                                  │
│  [Save this route]               │
│  [Explore from here]             │
│  [Done]                          │
│                                  │
└─────────────────────────────────┘
```

**Copy:**
- Title: "You've arrived"
- Summary: "Route completed" + stats
- Feedback: "How was your ride?"
- Emoji reactions: 😊 Great / 🙂 Good / 😐 Meh
- Actions:
  - "Save this route" (for later)
  - "Explore from here" (continue discovery)
  - "Done" (return home)

**Learning Loop:**
- Positive feedback → Remember route preferences
- Negative feedback → Avoid similar routes
- Saved routes → Add to "My Routes" collection

**User Feeling:**
"That was pleasant. I'd do this again."

---

## 4. ROUTE RECOMMENDATION LOGIC

### Decision Algorithm

**Input Parameters:**
1. User intent (eat, relax, culture, etc.)
2. Current location (GPS)
3. Battery level (e-bike + phone)
4. Time of day
5. Weather conditions
6. User history (if available)

**Processing Steps:**

```
Step 1: Define Search Radius
- Battery >80% → 10 km max
- Battery 50-80% → 5 km max
- Battery <50% → 2 km max
- Phone battery <20% → Disable exploration

Step 2: Query Mapping API
- Request 5-10 potential routes
- Filter: bike lanes >60%, traffic level low
- Prioritize: flat terrain, scenic value

Step 3: Query Places API
- Based on intent (e.g., "eat" → restaurants)
- Radius: within route corridor
- Filter: rating >4.0, currently open
- Limit: 10 candidates

Step 4: AI Curation
- Send to LLM:
  - Route options (5-10)
  - Place options (10)
  - User context (intent, time, weather)
- LLM returns:
  - 1 primary route + 3-5 stops
  - 2 alternative routes (optional)
  - Human explanation

Step 5: Validate & Present
- Check: route distance <15 km
- Check: stops <5
- Format: route card UI
- Present to user
```

### Prioritization Rules

**Routes ranked by:**
1. **Safety** (bike lane %, traffic level) – 40% weight
2. **Comfort** (elevation, surface quality) – 30% weight
3. **Scenic Value** (parks, waterfront, architecture) – 20% weight
4. **Relevance** (matches intent, time-appropriate) – 10% weight

**Stops ranked by:**
1. **Intent match** (café if user wants coffee) – 50% weight
2. **Location** (on or near route, not detour) – 30% weight
3. **Quality** (ratings, reviews, AI assessment) – 20% weight

### Constraints (HARD LIMITS)

- ❌ Never recommend routes >15 km unless explicitly requested
- ❌ Never recommend routes with >100m elevation gain for "relax" intent
- ❌ Never recommend busy roads if bike lane alternative exists
- ❌ Never show >5 stops per route
- ❌ Never recommend closed venues
- ❌ Never recommend "tourist trap" places (AI filters these)

---

## 5. PLACE RECOMMENDATION LOGIC

### When Used
- User doesn't want a full route
- User asks "What's nearby?"
- Quick decision needed (e.g., battery low)

### Selection Criteria

**Query Parameters:**
- Intent: eat, coffee, culture, relax, etc.
- Radius: 1-2 km (10 min cycling max)
- Current time: filter by opening hours

**AI Filtering:**
```
For each candidate place:
1. Check: Rating >4.0 AND currently open
2. Check: Not generic chain (prefer local)
3. Check: Matches user intent
4. Check: Safe cycling access (near bike lanes)
5. LLM evaluates:
   - Is this "premium" feeling?
   - Is description human, not promotional?
   - Does it fit "right now"?
```

**Output Format:**
```json
{
  "places": [
    {
      "name": "Dulce de Leche",
      "type": "Café",
      "distance_km": 0.8,
      "cycling_time_min": 5,
      "description": "Cozy, not touristy. Perfect for now.",
      "why_now": "Quiet afternoon spot. Less crowded than downtown.",
      "coordinates": {...}
    }
  ],
  "count": 3
}
```

### Presentation Rules

- **Always show 3 places max** (no endless scrolling)
- **Order by cycling time** (closest first)
- **Include "why now"** (contextual explanation)
- **No ratings/reviews shown** (we curate, user trusts)

### Voice Shortcuts

- "Petal, take me to the first one" → Navigate to #1
- "Petal, tell me more about the second" → Expand description
- "Petal, none of these" → Show 3 different options

---

## 6. VOICE-FIRST INTEGRATION (CRITICAL)

### Philosophy

Voice is not a "feature" — it's the **primary interface** while cycling.

**Design Principles:**
1. **Hands-free first** – Assume user is cycling, can't touch phone
2. **Interruption-aware** – Never interrupt while user is moving fast
3. **Context-aware** – Responses adapt to what's happening NOW
4. **Brief by default** – Max 15 seconds spoken
5. **Calm tone** – Never urgent, never pushy

### Voice Interaction Matrix

| User State | Voice Behavior |
|------------|----------------|
| **Cycling (moving)** | Silent. Visual cues only. |
| **Stopped (<30 sec)** | Silent. Wait for user to initiate. |
| **Stopped (>30 sec)** | Gentle suggestion: "There's a nice café nearby" |
| **Approaching stop** | Brief notification: "Next stop in 100 meters" |
| **At stop location** | "We're here. Want to know more?" |
| **User asks question** | Immediate response, stay active 10 sec |

### Voice Command Examples

#### Location & Context
```
User: "Petal, where are we?"
Petal: "El Carmen, near Plaza de la Virgen. Want to explore?"

User: "Petal, what's around here?"
Petal: "There's a hidden café 3 minutes away, and the old town market is just ahead."
```

#### Navigation Control
```
User: "Petal, skip this stop"
Petal: "Got it. Skipping the museum. Next stop is the park in 5 minutes."

User: "Petal, take me home"
Petal: "Navigating home. Fastest route, 12 minutes."
```

#### Discovery & Recommendations
```
User: "Petal, I'm hungry"
Petal: "There's a great local spot 4 minutes ahead. Paella and terrace. Want directions?"

User: "Petal, surprise me"
Petal: "How about a quiet route through the old town? 20 minutes, with a stop at a vintage bookshop."
```

#### Information Requests
```
User: "Petal, tell me about this place"
Petal: "Plaza de la Virgen. 13th century square, beautiful fountain. Local favorite, not touristy."

User: "Petal, is this safe?"
Petal: "Yes. Dedicated bike lane, low traffic. I've got you."
```

#### Assistance
```
User: "Petal, I need a restroom"
Petal: "Adding a café with facilities. 2 minutes ahead on your left."

User: "Petal, I'm tired"
Petal: "Want a shortcut home? Or I can find a nice place to rest."
```

### Voice Response Structure

**Every response follows this format:**
1. **Acknowledge** (1-2 words): "Got it." / "Sure." / "Okay."
2. **Action/Info** (1 sentence): What's happening or being shared
3. **Optional Follow-up** (if stopped): "Want me to start navigation?"

**Examples:**
- ❌ Bad: "I've successfully recalculated your route and removed the museum stop from your itinerary. Your next destination is Jardín del Turia, which is located 1.2 kilometers ahead."
- ✅ Good: "Skipping the museum. Next stop is the park, 5 minutes ahead."

### When Voice Is Silent

**No voice during:**
- User cycling >15 km/h
- Phone in pocket (screen off)
- User on phone call
- User hasn't interacted in >5 min

**Visual-only cues:**
- Turn-by-turn navigation arrows
- Upcoming stop previews
- Distance/time remaining

### Handling Interruptions

**Scenario: User asks question mid-navigation**
1. Pause navigation voice
2. Answer user question
3. Resume navigation (no recap needed if <10 sec pause)

**Scenario: User gets phone call**
1. Detect call start
2. Silence Petal completely
3. Resume when call ends (brief: "Back on route")

**Scenario: Ambient noise (wind, traffic)**
1. Increase TTS volume automatically
2. If recognition fails 2x: "I can't hear you. Try again when it's quieter."

---

## 7. AI ARCHITECTURE & APIs

### System Architecture Diagram (Text Format)

```
┌─────────────────────────────────────────────────────────────┐
│                        PETAL APP                             │
│                                                              │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   │
│  │   Voice      │   │   Route      │   │   Place      │   │
│  │   Input      │──▶│   Engine     │◀──│   Engine     │   │
│  └──────────────┘   └──────────────┘   └──────────────┘   │
│         │                   │                   │           │
└─────────┼───────────────────┼───────────────────┼───────────┘
          │                   │                   │
          │                   │                   │
          ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────┐
│                     API LAYER                                │
│                                                              │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   │
│  │   Gemini     │   │   Google     │   │   Google     │   │
│  │   LLM API    │   │   Maps API   │   │  Places API  │   │
│  │              │   │              │   │              │   │
│  │ • NLU        │   │ • Routes     │   │ • POI data   │   │
│  │ • Curation   │   │ • Bike lanes │   │ • Ratings    │   │
│  │ • Voice gen  │   │ • Elevation  │   │ • Hours      │   │
│  └──────────────┘   └──────────────┘   └──────────────┘   │
└─────────────────────────────────────────────────────────────┘
          │                   │                   │
          ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────┐
│                    PRIVACY LAYER                             │
│                                                              │
│  • GPS coordinates anonymized before sending to LLM         │
│  • User identity NEVER sent to external APIs                │
│  • History stored locally only                              │
│  • AI responses filtered before display                     │
└─────────────────────────────────────────────────────────────┘
```

### Component Responsibilities

#### 1. **Gemini LLM API** (Primary AI)

**What it does:**
- Natural language understanding (voice commands)
- Route explanation generation (human descriptions)
- Place curation (filter tourist traps, assess quality)
- Voice response generation (calm, contextual replies)

**What data is SENT:**
- User voice command (text only, after speech-to-text)
- Route options (from Maps API): distance, duration, bike lane %
- Place options (from Places API): name, type, rating
- Context: time of day, weather, general intent
- Location: City/neighborhood name ONLY (not exact GPS)

**What data is NOT SENT (Privacy-First):**
- Exact GPS coordinates
- User name or ID
- User history or saved routes
- Phone contacts
- Personal preferences (stored locally)

**API Configuration:**
```
Model: gemini-1.5-flash
Temperature: 0.3 (consistent, reliable)
Max tokens: 200 (brief responses)
Safety: High (block harmful content)
```

#### 2. **Google Maps API** (Routing Engine)

**What it does:**
- Generate cycling routes between A→B
- Provide bike lane data
- Calculate elevation profiles
- Estimate cycling time (adjusted for e-bike speed)

**Endpoints Used:**
- `Directions API` (mode: bicycling)
- `Elevation API` (route profile)
- `Distance Matrix API` (multi-stop routes)

**What data is SENT:**
- Origin & destination coordinates
- Waypoints (stops)
- Mode: "bicycling"
- Avoid: highways, tolls

**What data is NOT SENT:**
- User identity
- Purpose of trip
- User preferences

#### 3. **Google Places API** (POI Discovery)

**What it does:**
- Find nearby cafés, restaurants, cultural spots
- Provide ratings, reviews, opening hours
- Return photos, descriptions

**Endpoints Used:**
- `Nearby Search` (radius-based)
- `Place Details` (for selected venues)
- `Text Search` (for specific queries like "coffee")

**What data is SENT:**
- Search center (GPS coordinates)
- Radius (1-5 km)
- Type (e.g., "restaurant", "cafe")
- Opening now: true

**What data is NOT SENT:**
- User identity
- Why user is searching

### Data Flow Example

**Scenario:** User says "Petal, I want coffee"

```
1. Voice Input (on-device)
   ├─ Speech-to-text: "Petal, I want coffee"
   └─ Extract intent: "COFFEE"

2. Places API Query
   ├─ Search: type=cafe, radius=2km, open_now=true
   └─ Returns: 15 café candidates

3. Maps API Query (optional)
   ├─ Calculate cycling time to each café
   └─ Returns: 15 time estimates

4. Gemini LLM Curation
   ├─ Receives:
   │  - 15 café names, ratings, descriptions
   │  - User context: "afternoon, wants coffee, cycling"
   │  - Location: "El Carmen, Valencia" (not exact GPS)
   ├─ Prompt: "Select 3 best cafés for cycling break. Prefer local, not chains."
   └─ Returns: 3 curated cafés with human descriptions

5. App Displays
   ├─ Show 3 café cards
   └─ User taps "Navigate" → Maps API generates route
```

### AI Curation Prompt (Internal)

**Used for Route Selection:**
```
You are a discreet local concierge for cyclists in [City].

User context:
- Intent: [relax/eat/culture/explore]
- Time: [morning/afternoon/evening]
- Weather: [sunny/rainy/etc]
- Battery: [high/medium/low]

Available routes:
[List of 5-10 routes with stats: distance, bike lanes %, elevation, nearby POIs]

Task:
Select the 1 best route for this user RIGHT NOW.
Include 3-5 stops along the way that match their intent.

Output format (JSON):
{
  "route_id": "...",
  "title": "A quiet ride through the old town",
  "description": "Mostly bike lanes, flat terrain, with a stop at a hidden café.",
  "stops": [
    {
      "name": "...",
      "type": "...",
      "why": "Perfect for a break"
    }
  ]
}

Rules:
- Title: 6-8 words, human, not promotional
- Description: 1 sentence, calm tone
- Stops: Max 5, on or near route
- No tourist traps, prefer local favorites
- Prioritize safety and comfort over speed
```

**Used for Place Curation:**
```
You are a discreet local concierge for cyclists.

User wants: [coffee/food/culture/etc]
Location: [neighborhood, city]
Time: [afternoon]
Available options: [list of 10 places with ratings]

Task:
Select the 3 best places RIGHT NOW.

Output format (JSON):
{
  "places": [
    {
      "name": "Dulce de Leche",
      "description": "Cozy, not touristy. Perfect for now.",
      "why_now": "Quiet afternoon spot. Less crowded than downtown."
    }
  ]
}

Rules:
- Description: 1 sentence, max 8 words
- Why now: Explain why it fits THIS moment
- No chains, prefer local
- No promotional language
- Tone: calm, confident, human
```

### Response Filtering & Validation

**Before showing ANY AI output to user:**

1. **JSON Validation**
   - Check: Valid JSON structure
   - Check: Required fields present
   - Fallback: Use template if invalid

2. **Content Safety**
   - Check: No harmful/inappropriate content
   - Check: No promotional spam
   - Filter: Remove URLs, phone numbers

3. **Tone Validation**
   - Check: Matches Petal voice (calm, premium)
   - Reject if: Uses "amazing", "must-see", "don't miss"
   - Reject if: Too long (>20 words for descriptions)

4. **Accuracy Check**
   - Verify: Place is actually open (cross-check with Places API)
   - Verify: Route distance matches Maps API data
   - Reject: If discrepancy >20%

**If any check fails:**
- Log error (for debugging)
- Use fallback template: "I found a nice place nearby. Want directions?"
- Never show raw AI output

---

## 8. PROMPT ENGINEERING (AI Personality)

### Petal Voice Guidelines

**Tone:**
- Calm, never urgent
- Confident, not bossy
- Friendly, not overly casual
- Premium, not pretentious

**Personality:**
"A discreet local concierge who knows cycling."

**DO:**
✅ "There's a quiet café 3 minutes ahead."
✅ "This route is mostly flat and shaded."
✅ "Perfect spot for a break."
✅ "I've got you."

**DON'T:**
❌ "You absolutely MUST try this amazing place!"
❌ "This is the #1 rated café in Valencia!"
❌ "Don't miss this incredible hidden gem!"
❌ "Turn left in 47 meters at the traffic signal."

### Response Length Constraints

| Context | Max Length | Example |
|---------|------------|---------|
| Voice response (cycling) | 15 sec / ~30 words | "Got it. Skipping the museum. Next stop is the park in 5 minutes." |
| Place description (text) | 1 sentence / 8 words | "Cozy, not touristy. Perfect for now." |
| Route description (text) | 1 sentence / 12 words | "A quiet ride through El Carmen with a stop at a hidden café." |
| Why now explanation | 1 sentence / 10 words | "Quiet afternoon spot. Less crowded than downtown." |

### Forbidden Language

**Never use:**
- "Amazing", "Incredible", "Must-see", "Don't miss"
- "Rated #1", "Best in [city]", "Top-rated"
- Star ratings in voice responses (e.g., "4.5 stars")
- Technical jargon ("POI", "ETA", "waypoint")
- Tourist clichés ("hidden gem", "off the beaten path")

**Instead use:**
- "Nice", "Quiet", "Cozy", "Worth it"
- "Local favorite", "Popular with locals"
- Descriptions: "Great coffee", "Beautiful terrace"
- Human language: "place", "time", "stop"
- Simple: "Not touristy", "Less crowded", "Calm"

### System Prompt Template (Final Version)

```
You are Petal, a voice assistant for urban cyclists.

Your personality:
- Calm and reassuring, never urgent
- Premium but not pretentious
- Local expert, not a tourist guide
- Concise, never verbose

Your role:
- Recommend routes and places cyclists will enjoy
- Prioritize safety, comfort, and cycling pleasure
- Make decisions FOR the user (reduce choice fatigue)
- Speak like a discreet concierge, not a GPS

Communication rules:
- Voice responses: Max 30 words, 15 seconds
- Text descriptions: 1 sentence, 8-12 words
- Tone: Confident, calm, human
- Forbidden: "Amazing", ratings, tourist jargon
- Always explain "why now" (context matters)

Safety first:
- If battery low: Recommend short routes only
- If weather bad: Suggest covered paths
- If user tired: Offer shortcuts or rest stops
- If GPS unstable: Wait before recommendations

You are helpful, trustworthy, and premium.
```

---

## 9. UI / DESIGN PRINCIPLES

### Visual Identity

**Color Palette:**
- **Primary:** Petal Blue (#2563EB) – Accent only (buttons, icons)
- **Background:** Soft Gray-Blue (#F8FAFC) – Main app background
- **Cards:** Pure White (#FFFFFF) – All content cards
- **Text Primary:** Deep Slate (#0F172A) – Headlines, body
- **Text Secondary:** Medium Gray (#64748B) – Descriptions, labels
- **Success:** Elegant Green (#16A34A) – Active states
- **Error:** Refined Red (#DC2626) – Warnings

**Typography:**
- **Headings:** Inter Bold, 20-28sp
- **Body:** Inter Regular, 14-16sp
- **Labels:** Inter Medium, 12-14sp
- **Voice prompts:** Inter Regular, 14sp, 70% opacity

### Layout Rules

**Spacing:**
- Screen padding: 16dp all sides
- Card padding: 20-24dp internal
- Element spacing: 8dp (tight), 16dp (standard), 24dp (loose)

**Cards:**
- Corner radius: 16dp (consistent)
- Elevation: 2dp (subtle shadow)
- Border: None (clean edges)
- Background: Pure white

**Buttons:**
- Primary: Petal Blue fill, white text, 56dp height
- Secondary: Outline (1.5dp), Petal Blue stroke, 48dp height
- Tertiary: Text link, Petal Blue, no background
- Corner radius: 12dp
- Typography: Inter Medium, 16sp

**Icons:**
- Size: 24dp (UI), 32dp (cards)
- Style: Rounded, not sharp
- Color: Single tone (no gradients)
- Usage: Contextual (🍽️ eat, 🌳 relax, etc.)

### Screen Patterns

**Header Pattern:**
```
┌─────────────────────────────────┐
│ [Back] Title          [Action]  │
└─────────────────────────────────┘
```
- Left: Back arrow (if needed)
- Center: Title (20sp Bold)
- Right: Action icon (optional)

**Card Pattern:**
```
┌─────────────────────────────────┐
│ [Icon] Title                     │
│        Subtitle                  │
│        Description (1 sentence)  │
│                                  │
│ [Primary Button] [Secondary]    │
└─────────────────────────────────┘
```

**Empty State Pattern:**
```
┌─────────────────────────────────┐
│                                  │
│        [Illustration]            │
│                                  │
│    Short explanation             │
│    What user can do next         │
│                                  │
│    [Primary Action]              │
│                                  │
└─────────────────────────────────┘
```

### Animation Principles

**Transitions:**
- Duration: 200-300ms
- Easing: Ease-out (natural deceleration)
- No bounces or springs (premium = calm)

**Screen Changes:**
- Fade + slide (no abrupt cuts)
- Direction: Left→right for back, right→left for forward

**Loading States:**
- Subtle spinner (Petal Blue)
- Text: "Finding the best route..." (not "Loading...")
- No skeleton screens (prefer fade-in)

### What to AVOID

❌ **No Clutter:**
- Long lists
- Infinite scroll
- Too many options
- Dense information

❌ **No Gamification:**
- Points, badges, streaks
- Leaderboards
- "Achievements"
- Progress bars (except navigation)

❌ **No Ads or Promotions:**
- Sponsored locations
- "Promoted" labels
- Banner ads
- Partner placements

❌ **No Aggressive Patterns:**
- Bright colors (neon, saturated)
- Sharp corners
- Heavy shadows
- Busy backgrounds

---

## 10. EDGE CASES & SAFETY

### Battery Management

| Battery Level | Behavior |
|---------------|----------|
| **Phone >50%** | Normal operation |
| **Phone 20-50%** | Shorten routes, warn before long trips |
| **Phone <20%** | Disable exploration, offer "navigate home" only |
| **E-bike >80%** | Allow adventurous routes (up to 15 km) |
| **E-bike 50-80%** | Moderate routes (up to 10 km) |
| **E-bike <50%** | Conservative routes (up to 5 km), prioritize charging stations |
| **E-bike <20%** | Emergency mode: "Find nearest charging station" |

**Voice Alerts:**
- At 30%: "Your battery is getting low. Want a shorter route?"
- At 20%: "Battery low. I recommend heading back soon."
- At 10%: "Battery critical. Navigate home?"

### GPS Instability

| GPS State | Behavior |
|-----------|----------|
| **Accurate (<10m)** | Normal operation |
| **Moderate (10-50m)** | Show visual warning: "GPS signal weak" |
| **Poor (>50m)** | Pause recommendations: "Waiting for better signal..." |
| **No signal** | Offline mode: "Can't locate you. Try again when signal improves." |

**Recovery:**
- When signal returns: "Back online. Still want to explore?"
- Don't recalculate automatically (user may have moved)

### User Speed Detection

| Speed | Petal Behavior |
|-------|----------------|
| **Stopped** | Active: Offer suggestions, respond to voice |
| **Slow (<5 km/h)** | Gentle: Show visual cues, wait for user initiation |
| **Cycling (5-20 km/h)** | Silent: Visual-only navigation, no voice interruptions |
| **Fast (>20 km/h)** | Alert mode: Disable non-critical features, focus on safety |

**Safety Override:**
- If user cycling fast + asks question: "I'll answer when you slow down. Stay safe."

### Unsafe Areas

**Detection:**
- Cross-reference GPS with crime data (if available)
- Traffic density (avoid busy roads at night)
- User reports (crowdsourced safety data)

**Response:**
```
Scenario: Route passes through flagged area
┌─────────────────────────────────┐
│ ⚠️ Safety Notice                │
│                                  │
│ This route includes a section   │
│ with heavy traffic. Want a      │
│ safer alternative?              │
│                                  │
│ [Find safer route]              │
│ [Continue anyway]               │
└─────────────────────────────────┘
```

**Voice Response:**
- "This area has heavy traffic. I can find a safer route."

### Missing Data

| Missing | Fallback |
|---------|----------|
| **No bike lane data** | Default to "route quality unknown" |
| **No POI data** | Skip recommendations, offer general navigation |
| **No weather data** | Proceed without weather-based filtering |
| **No elevation data** | Warn: "I can't check if this route is hilly" |

**User Communication:**
- Always be honest: "I don't have data for this area yet."
- Offer alternative: "Want me to find a route I know better?"

### Network Errors

**API Failures:**
```
┌─────────────────────────────────┐
│ Connection issue                 │
│                                  │
│ Can't reach route service.      │
│ Check your connection?          │
│                                  │
│ [Try again]                     │
│ [Use saved routes]              │
└─────────────────────────────────┘
```

**Voice Response:**
- "I can't connect right now. Want to try a saved route?"

**Retry Logic:**
- Auto-retry once after 3 seconds
- If fails again: User-initiated retry only
- Cache last successful routes (offline mode)

### Weather Extremes

| Weather | Petal Behavior |
|---------|----------------|
| **Rain** | Prioritize covered paths, shorter routes |
| **Heavy rain** | Suggest: "Weather's rough. Want to wait?" |
| **Heat (>35°C)** | Recommend shaded routes, more rest stops |
| **Cold (<5°C)** | Shorter routes, indoor destination options |
| **Wind (>30 km/h)** | Avoid exposed areas, suggest: "Windy today. Protected route?" |

---

## 11. DELIVERABLES SUMMARY

### For Designers (Figma)

**UI Screens to Design:**
1. Entry point / Home integration
2. Intent selection (6 intent cards)
3. Route recommendation card (main screen)
4. Route details view
5. Place recommendation cards (3-card layout)
6. Navigation interface (full-screen map + voice bar)
7. End-of-route feedback
8. Empty states (no GPS, no data, no results)
9. Error states (network, battery, safety warnings)

**Design System:**
- Color palette (8 colors)
- Typography scale (3 sizes)
- Button styles (3 variants)
- Card components (route, place, stop)
- Icon set (intent icons, navigation icons)
- Animation specs (transitions, loading)

**Prototype Flows:**
- Happy path: Home → Intent → Route → Navigation → End
- Voice path: Home → Voice command → Direct navigation
- Alternative: Home → Intent → Places (no route)

### For Developers (Flutter/Web)

**API Integrations:**
1. **Google Maps API**
   - Directions API (bicycling mode)
   - Elevation API
   - Distance Matrix API

2. **Google Places API**
   - Nearby Search
   - Place Details
   - Photos API

3. **Gemini LLM API**
   - Text generation endpoint
   - JSON mode
   - Safety filters

**State Management:**
- User context (location, battery, speed)
- Route state (active, paused, completed)
- Voice state (listening, silent, responding)
- Cache (saved routes, recent searches)

**Key Classes/Modules:**
```
RouteEngine
├─ generateRoutes(intent, context) → Route[]
├─ curateWithAI(routes, places) → RecommendedRoute
└─ calculateSafety(route) → SafetyScore

PlaceEngine
├─ findNearbyPlaces(intent, radius) → Place[]
├─ curateWithAI(places, context) → RecommendedPlace[]
└─ filterByOpenHours(places) → Place[]

VoiceController
├─ onVoiceCommand(text) → Action
├─ respondWithTTS(text)
├─ detectUserState() → State (cycling, stopped, fast)
└─ manageInterruptions()

ContextManager
├─ getCurrentLocation() → LatLng
├─ getBatteryLevel() → int
├─ getWeather() → Weather
└─ getUserSpeed() → float
```

### For AI Engineers

**LLM Integration:**

1. **Prompt Templates**
   - Route curation prompt (see Section 7)
   - Place curation prompt (see Section 7)
   - Voice response prompt (see Section 8)

2. **Response Validation**
   - JSON schema validation
   - Content safety checks
   - Tone validation (reject promotional language)

3. **Context Injection**
   - User context format: `{intent, time, weather, battery}`
   - Location anonymization: City/neighborhood only
   - History: Local storage, never sent to API

4. **Performance Optimization**
   - Cache common responses (e.g., "What's nearby?")
   - Batch API calls when possible
   - Timeout: 5 seconds max (fallback to templates)

**Testing Scenarios:**
- Edge cases: No data, poor GPS, API errors
- Safety filters: Reject inappropriate suggestions
- Tone consistency: Audit 100+ responses for brand alignment

### For Product Stakeholders

**Launch Metrics:**

| Metric | Target (3 months) |
|--------|-------------------|
| **Adoption Rate** | 40% of active users try Routes & Places |
| **Voice Usage** | 60% of route starts initiated by voice |
| **Route Acceptance** | 75% follow primary recommendation (don't seek alternatives) |
| **Completion Rate** | 80% complete recommended routes |
| **User Satisfaction** | NPS >50, keywords: "calm", "trust", "easy" |

**Success Criteria:**
- Users say: "I don't need to think about it. Petal just knows."
- Benchmark: Better than Google Maps for cycling discovery (qualitative)
- Safety: Zero incidents attributable to route recommendations

**Future Evolution Roadmap:**

**Phase 1: Launch (Months 1-3)**
- Core UX: Intent → Route → Navigation
- Voice: Basic commands (navigate, skip, tell me)
- AI: Gemini curation, Google APIs
- Cities: Valencia (pilot)

**Phase 2: Expansion (Months 4-6)**
- More intents: Shopping, nightlife, family-friendly
- Personalization: Learn user preferences over time
- Social: Share routes with friends
- Cities: Barcelona, Madrid, Seville

**Phase 3: Premium (Months 7-12)**
- Petal Turismo: Guided multi-day cycling tours
- Premium tier: Exclusive routes, priority access
- Partnerships: Local businesses (cafés, bike shops)
- Offline mode: Downloaded routes for areas without signal

**Phase 4: Platform (Year 2+)**
- User-generated routes (curated by Petal AI)
- Community: Local cyclist meetups, events
- Wearables: Smartwatch integration, AR glasses
- International: European cities, US cities

---

## APPENDICES

### A. Voice Command Reference (Complete List)

**Navigation:**
- "Petal, where are we?"
- "Petal, take me home"
- "Petal, navigate to [place]"
- "Petal, skip this stop"
- "Petal, add a stop"
- "Petal, end navigation"

**Discovery:**
- "Petal, surprise me"
- "Petal, I'm hungry"
- "Petal, I want coffee"
- "Petal, show me something cultural"
- "Petal, find a quiet spot"
- "Petal, what's around here?"

**Information:**
- "Petal, tell me about this place"
- "Petal, how far to the next stop?"
- "Petal, is this route safe?"
- "Petal, what's the weather?"
- "Petal, battery status"

**Assistance:**
- "Petal, I need a restroom"
- "Petal, I'm tired"
- "Petal, I'm lost"
- "Petal, find water"
- "Petal, where can I charge?"

**Control:**
- "Petal, pause"
- "Petal, resume"
- "Petal, louder/quieter"
- "Petal, stop talking"

### B. Error Message Copy (Complete)

**Network Errors:**
- "Can't connect right now. Check your signal?"
- "Lost connection. Try again in a moment."

**GPS Errors:**
- "Waiting for GPS signal..."
- "Can't locate you. Try moving to an open area."

**Battery Warnings:**
- "Battery's low. Want a shorter route?"
- "Battery critical. I recommend heading back."

**API Failures:**
- "Route service unavailable. Want to try a saved route?"
- "Can't find places right now. Try again later?"

**User Input Errors:**
- "I didn't catch that. Try again?"
- "I can't hear you. Try when it's quieter."

**Safety Warnings:**
- "This route has heavy traffic. Want a safer alternative?"
- "Weather's rough. Sure you want to ride now?"

### C. Design Assets Needed

**Icons (24dp):**
- 🍽️ Eat (fork + knife)
- 🏛️ Culture (column)
- 🌳 Relax (tree)
- ☕ Coffee (cup)
- 🎨 Art (palette)
- ✨ Surprise (sparkle)
- 🎙️ Microphone (voice)
- 🚴 Cycling (bike)
- 📍 Location (pin)

**Illustrations:**
- Empty state (no routes)
- No GPS signal
- Battery low
- Connection error
- Success (route completed)

**Maps Styling:**
- Custom Mapbox/Google Maps style (muted colors)
- Petal Blue for active route
- Gray for alternative routes
- Green dots for stops

---

## FINAL NOTES

This specification is designed to be **directly actionable** by:
- Designers creating Figma mockups
- Developers building Flutter/Web app
- AI engineers integrating Gemini API
- Product managers defining roadmap

**Key Success Factor:**
The experience must feel **effortless, calm, and intelligent** — not like a feature, but like a trusted companion.

**Brand Alignment:**
Everything reinforces Petal's premium, cycling-first positioning. This is not Google Maps with a bike icon. This is a fundamentally different approach to urban mobility.

---

**Document Status:** ✅ Ready for Implementation
**Next Steps:** Design review → Prototyping → Development sprint planning

---

*"Less choice. Better decision."*
– Petal Routes & Places

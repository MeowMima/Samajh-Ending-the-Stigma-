# SAMAJH: "Ending the Stigma"
____________________________________________________________________
An AI - Powered Mental Health Awareness and Emotional Reflection platform designed to make mental health conversations more accessible, empathetic and stigma-free for Indian users.

Samajh combines:
- Conversational AI Support 
- Reflective Journaling
- Educational Mental Health Resources
- AI - Powered Recommendations
- Helpline Numbers

into a single integrated platform!

# VISION
_________________________________________________________________________
Currently most mental health platforms are built with the primary focus on therapy businesses or clinical services.

Samajh is built with the vision of:
> to create a safe, educational and emotionally supportive digital space where people can feed their curiosity about mental health without any fear of judgement, shame or taboo

Especially designed for:
- first-time help seekers
- youth of India
- emotionally overwhelmed users
- user hesitant to seek therapy(encourages them positively)
- mental health awareness in the Indian context

# FEATURES
__________________________________________________________________________
## Saathi — AI Emotional Companion
- Empathetic conversational chatbot
- Context-aware emotional responses
- Crisis-sensitive prompts
- Built using OpenRouter API + Flask

## Journal Reflector
- Users write emotional journal entries
- AI analyzes emotional themes
- Generates:
  - summaries
  - reflections
  - emotional insights
  - supportive suggestions

Journal entries are stored using SQLite.

## AI Recommendation System
- AI-generated mental health learning guidance
- Dynamic educational recommendations
- Real YouTube video integration using YouTube Data API
- Topic-based emotional exploration

## Learning Hub
- AI-generated educational articles
- Mental health awareness content
- Stigma reduction focused content
- Simplified psychology explanations

# TECH STACK
______________________________________________________________________________

## Backend
- Python
- Flask
- SQLite

## APIs & AI
- OpenRouter API
- YouTube Data API v3

## Frontend
- HTML
- CSS
- JavaScript

## Deployment
- Render

## Version Control
- Git & GitHub

# SYSTEM ARCHITECTURE
_________________________________________________________________________________

    User Interface
         ↓
    Flask Backend
         ↓
    AI Processing Layer(OpenRouter)
         ↓
    Response Parsing & Processing
         ↓
    SQLite Database Storage
         ↓
    Frontend Rendering

# PROJECT STRUCTURE
____________________________________________________________________________________
Samajh/
|
|-app.py
|-requirements.txt
|-runtime.txt
|-README.md
|-Procfile
|-.gitignore
|-.env
|-users.db
|
|-static/
|  |-article.css
|  |-design.css
|  |-explore.css
|  |-intro.css
|  |-journal.css
|  |-learn.css
|  |-saathi.css
|
|-templates
|  |-article.html
|  |-explore.html
|  |-intro.html
|  |-journal.html
|  |-learn.html
|  |-saathi.html
|

# Environment Variables

Create a `.env` file in the project root:

```env
OPENROUTER_API_KEY=your_openrouter_api_key
YOUTUBE_API_KEY=your_youtube_api_key
SECRET_KEY=your_secret_key
```

# Running Locally

## Clone Repository

```bash
git clone <your-github-link>
cd Samajh
```

## Create Virtual Environment

```bash
python -m venv venv
```

## Activate Virtual Environment

### Windows
```bash
venv\Scripts\activate
```

### Mac/Linux
```bash
source venv/bin/activate
```

## Install Dependencies

```bash
pip install -r requirements.txt
```

## Run Application

```bash
python app.py
```

Application runs on:

```
http://localhost:5000
```

# Deployment
The application is deployed using Render.

# Future Scope
- User authentication system
- Personalized dashboards
- Mood tracking analytics
- Voice-based AI companion
- Multilingual Indian language support
- AI memory & personalization
- Therapist/community integration
- Mental health assessment modules
- Mobile application version

# ❗Important Disclaimer❗
_______________________________________________________________________
Samajh is an educational and emotional support platform.

It is NOT a substitute for:
- professional therapy
- psychiatric treatment
- emergency mental health services

# 👩‍💻 Author
__________________________________________________________________________
Mimansa Gupta
AI/ML + Applied AI Engineering Enthusiast  
Focused on building AI-powered human-centered products.
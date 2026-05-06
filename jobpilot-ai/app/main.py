import uvicorn
from fastapi import FastAPI
from app.api import resume_api

app = FastAPI(title="JobPilot AI Service")

app.include_router(resume_api.router,prefix="/api/resume/ai",tags=["Resume"])

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
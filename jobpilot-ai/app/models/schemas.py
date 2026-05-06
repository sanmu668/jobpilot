from pydantic import BaseModel
from typing import List, Optional


class ResumeRequest(BaseModel):
    text: str


class Education(BaseModel):
    school: Optional[str]
    degree: Optional[str]
    major: Optional[str]


class Experience(BaseModel):
    company: Optional[str]
    position: Optional[str]
    duration: Optional[str]
    description: Optional[str]


class ResumeResponse(BaseModel):
    name: Optional[str]
    phone: Optional[str]
    email: Optional[str]
    skills: List[str] = []
    education: List[Education] = []
    experience: List[Experience] = []

class DimensionItem(BaseModel):
    name: str
    score: int

class RadarItem(BaseModel):
    name: str
    score: int

class SkillsItem(BaseModel):
    matched: List[str]
    partial: List[str]
    missing: List[str]

class SuggestionItem(BaseModel):
    type: str
    content: str

class MatchRequest(BaseModel):
    resume_text: str
    job_description: str
    skill_tags: List[str] = []


class MatchResponse(BaseModel):
    score: int
    level: str
    summary: str

    dimensions: List[DimensionItem]
    radar: List[RadarItem]
    skills: SkillsItem
    suggestions: List[SuggestionItem]

    advantages: List[str] = []
    disadvantages: List[str] = []

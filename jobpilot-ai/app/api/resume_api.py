from fastapi import APIRouter
from app.models.schemas import ResumeRequest
from app.models.schemas import MatchRequest, MatchResponse
from app.service.match_service import analyze_match
from app.service.resume_parser import parse_resume

router = APIRouter()


@router.post("/parse")
async def parse_resume_api(req: ResumeRequest):
    result = await parse_resume(req.text)
    return result


@router.post("/analyze", response_model=MatchResponse)
async def match_analyze(req: MatchRequest):
    result = await analyze_match(req)
    return result

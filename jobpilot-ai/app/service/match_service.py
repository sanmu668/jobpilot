import json
import re

from app.prompts.match_prompt import build_prompt
from app.utils.ai_client import call_llm_async


async def analyze_match(req):
    prompt = build_prompt(
        req.resume_text,
        req.job_description,
        req.skill_tags
    )

    response = await call_llm_async(prompt)
    print("AI分析原始是：", response)
    print("类型:", type(response))
    print("=====================================")
    try:
        data = extract_json(response)
    except Exception as e:
        print("JSON分析结果获取失败:", e)
        return {
            "score": 0,
            "advantages": [],
            "disadvantages": [],
            "suggestions": ["AI分析失败，请重试"]
        }

    print(f"AI处理后返回的数据：{data}")
    return data

def extract_json(text: str):
    """
    从LLM返回中提取JSON
    """
    if isinstance(text, list):
        if len(text) > 0 and "text" in text[0]:
            text = text[0]["text"]
        else:
            raise Exception("LLM返回list结构异常")

    if isinstance(text,dict):
        return text
    if not isinstance(text,str):
        text = str(text)

    match = re.search(r"\{.*\}", text, re.DOTALL)

    if not match:
        raise Exception("未找到JSON")

    json_str = match.group()

    return json.loads(json_str)
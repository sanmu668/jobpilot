import json
import re
from app.utils.ai_client import call_llm_async


def load_prompt(text: str):
    with open("app/prompts/resume_parse_prompt.txt", "r", encoding="utf-8") as f:
        template = f.read()
    return template.replace("{{resume_text}}", text[:6000])


def _extract_json_str(result) -> str:
    if isinstance(result, list):
        first_item = result[0] if result else {}
        if isinstance(first_item, dict) and "text" in first_item:
            return first_item["text"]
        return ""
    if isinstance(result, dict):
        return result.get("text", "")
    if isinstance(result, str):
        return result
    return ""


async def parse_resume(text: str):
    prompt = load_prompt(text)
    result = await call_llm_async(prompt)
    print(f"AI解析返回的结果：{result}")
    print("=======================")
    try:
        json_str = _extract_json_str(result)
        clean_result = re.sub(r"```json|```", "", json_str).strip()
        data = json.loads(clean_result)
        print(f"清洗后推送的数据：{data}")
        return json.dumps(data, ensure_ascii=False)
    except Exception as e:
        print("解析异常：", str(e))
        return json.dumps({
            "name": "", "phone": "", "email": "",
            "skills": [], "education": [], "experience": []
        }, ensure_ascii=False)


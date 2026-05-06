import asyncio
import dashscope
from app.config.settings import settings

dashscope.base_http_api_url = "https://dashscope.aliyuncs.com/api/v1"
dashscope.api_key = "sk-32dfa3e8d91f480dbb1c62a1fedc991f"


def call_llm(prompt: str):
    try:
        response = dashscope.MultiModalConversation.call(
            model=settings.MODEL,
            messages=[
                {"role": "system", "content": "你是一个专业的简历解析助手"},
                {"role": "user", "content": prompt}
            ],
            temperature=settings.TEMPERATURE
        )
        if response.status_code == 200:
            return response.output.choices[0].message.content
        else:
            return f"调用失败: {response.code} - {response.message}"
    except Exception as e:
        return f"异常: {str(e)}"


async def call_llm_async(prompt: str):
    """
    将同步阻塞的 dashscope 调用放入线程池运行，
    避免占用 FastAPI 的异步事件循环。
    """
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(None, call_llm, prompt)
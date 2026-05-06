import os


class Settings:
    OPENAI_API_KEY = os.getenv("DASHSCOPE_API_KEY")
    MODEL = "qwen3.6-plus"
    TEMPERATURE = 0.2


settings = Settings()

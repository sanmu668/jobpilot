def build_prompt(resume_text, job_description, skill_tags):
    return f"""
    你是一个专业招聘分析专家，请分析“简历 + 岗位”的匹配情况。

⚠️ 要求：
- 必须适用于所有行业（IT / 金融 / 产品 / 设计等）
- 返回结构必须通用、可扩展
- 不允许输出解释说明
- 必须返回合法JSON

请输出以下结构：

{{
    "score": xx,
    "level": "高匹配/中等匹配/低匹配",
    "summary": "一句话总结匹配情况",

    "dimensions": [
        {{"name": "维度名称", "score": xx}}
    ],

    "radar": [
        {{"name": "能力名称", "score": xx}}
    ],

    "skills": {{
        "matched": [],
        "partial": [],
        "missing": []
    }},

    "suggestions": [
        {{
            "type": "建议分类（技能/项目/经验/表达）",
            "content": "具体优化建议"
        }}
    ],

    "advantages": [
        "候选人的核心优势1",
        "候选人的核心优势2"
    ],

    "disadvantages": [
        "候选人的主要不足1",
        "候选人的主要不足2"
    ]
}}

规则：
1. dimensions数量3-5个，根据岗位自动生成
2. radar反映核心能力
3. skills必须分类
4. 所有字段必须存在
5. 分数范围0-100
6. suggestions数量3-5个，根据岗位生成需要的技能强化建议
7. xx是根据分析结果对应指标的分数，"score": xx————是简历整体打分，分数范围0-100
8. advantages列出2-4条候选人与岗位匹配的核心优势（字符串列表）
9. disadvantages列出2-4条候选人与岗位的主要差距或不足（字符串列表）
====================
简历内容：
{resume_text}

岗位要求：
{job_description}

简历技能标签：
{skill_tags}
"""
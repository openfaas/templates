# author: Justin Guese, 11.3.22, justin@datafortress.cloud
from os import environ
import glob
from fastapi import FastAPI, Request
from fastapi.openapi.docs import get_swagger_ui_html
from function.handler import router

app = FastAPI()
app.include_router(router)

# required to render /docs path
@app.get("/docs", include_in_schema=False)
async def custom_swagger_ui_html(req: Request):
    root_path = req.scope.get("root_path", "").rstrip("/")
    openapi_url = root_path + app.openapi_url
    return get_swagger_ui_html(
        openapi_url=openapi_url,
        title="API",
    )
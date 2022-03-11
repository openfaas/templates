# Copyright (c) Craig Dalton 2019. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.
import json

from fastapi import FastAPI
from fastapi.openapi.utils import get_openapi
from function import handler
from starlette import status
from starlette.responses import HTMLResponse


app = FastAPI(docs_url=None, redoc_url=None)


def get_swagger_ui_html(
    *,
    openapi_spec: str,
    title: str,
    swagger_js_url: str = "https://cdn.jsdelivr.net/npm/swagger-ui-dist@3/swagger-ui-bundle.js",
    swagger_css_url: str = "https://cdn.jsdelivr.net/npm/swagger-ui-dist@3/swagger-ui.css",
    swagger_favicon_url: str = "https://raw.githubusercontent.com/openfaas/docs/master/docs/images/favicon.ico",
) -> HTMLResponse:
    """A tweaked fastapi.openapi.docs.get_swagger_ui_html to generate from raw JSON as opposed to using a url"""

    html = f"""
    <!DOCTYPE html>
    <html>
    <head>
    <link type="text/css" rel="stylesheet" href="{swagger_css_url}">
    <link rel="shortcut icon" href="{swagger_favicon_url}">
    <title>{title}</title>
    </head>
    <body>
    <div id="swagger-ui">
    </div>
    <script src="{swagger_js_url}"></script>
    <!-- `SwaggerUIBundle` is now available on the page -->
    <script>
    const spec = JSON.parse(`
        {openapi_spec}
    `)
    const ui = SwaggerUIBundle({{
        spec: spec,
        dom_id: '#swagger-ui',
        presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIBundle.SwaggerUIStandalonePreset
        ],
        layout: "BaseLayout",
        deepLinking: true
    }})
    </script>
    </body>
    </html>
    """
    return HTMLResponse(html)


def custom_openapi():
    """Generates schema from OpenFaas function particulars"""
    if app.openapi_schema:
        return app.openapi_schema
    openapi_schema = get_openapi(
        title=handler.FUNCTION_NAME,
        version=f"v{handler.FUNCTION_VERSION}",
        routes=app.routes,
    )
    paths = openapi_schema["paths"]
    upd_paths = {}
    # Supplement path specs
    for key in paths:
        path = paths[key]
        for method in path:
            path[method]["tags"] = ["Function Definitions"]
        # Modify path(s) to account for function being exposed
        # behind OpenFaas gateway
        rel_path = f"/function/{handler.FUNCTION_NAME}"
        if key.startswith(rel_path):
            upd_paths[key] = path
        else:
            rel_path = f"{rel_path}{key}"
            upd_paths[rel_path] = path
    openapi_schema["paths"] = upd_paths
    app.openapi_schema = openapi_schema
    return app.openapi_schema


app.openapi = custom_openapi


@app.get(
    "/",
    response_class=HTMLResponse,
    summary="Function Swagger UI Doc",
    response_description="Swagger UI documentation rendered as HTML (for consumption directly in a web browser)",
)
async def swagger_ui_html():
    openapi_html = get_swagger_ui_html(
        openapi_spec=json.dumps(app.openapi()),
        title=f"OpenFaas function: {handler.FUNCTION_NAME.title()}",
    )
    return openapi_html


@app.post(
    "/",
    status_code=status.HTTP_200_OK,
    response_model=handler.ResponseModel,
    summary=handler.FUNCTION_SUMMARY,
    response_description=handler.FUNCTION_RESPONSE_DESC,
)
def handle_request(
    *,
    req: handler.RequestModel,
):
    return handler.handle(req)


@app.get("/swagger.json", include_in_schema=False)
async def swagger_json():
    return app.openapi()

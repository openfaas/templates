
def handle(request):
    """handle a request to the function
    Args:
        req (str): request body
    """

    return request.get_data()

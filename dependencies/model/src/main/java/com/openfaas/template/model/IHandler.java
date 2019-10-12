// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.template.model;

public interface IHandler {
    com.openfaas.template.model.IResponse Handle(com.openfaas.template.model.IRequest request);
}


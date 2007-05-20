/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ws.transport;

import java.io.IOException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Abstract base class for {@link WebServiceConnection} implementations.
 *
 * @author Arjen Poutsma
 */
public abstract class AbstractWebServiceConnection implements WebServiceConnection {

    public final void send(WebServiceMessage message) throws IOException {
        TransportOutputStream tos = createTransportOutputStream();
        try {
            message.writeTo(tos);
            tos.flush();
        }
        finally {
            tos.close();
        }
        onSend(message);
    }

    public final WebServiceMessage receive(WebServiceMessageFactory messageFactory) throws IOException {
        TransportInputStream tis = createTransportInputStream();
        if (tis == null) {
            return null;
        }
        WebServiceMessage message = null;
        try {
            message = messageFactory.createWebServiceMessage(tis);
        }
        finally {
            tis.close();
        }
        onReceive(message);
        return message;
    }

    /**
     * Returns a <code>TransportOutputStream</code> for the given message. Called from {@link
     * #send(WebServiceMessage)}.
     *
     * @return the output stream
     * @throws IOException when an I/O exception occurs
     */
    protected abstract TransportOutputStream createTransportOutputStream() throws IOException;

    /**
     * Called when the given message has been written to the <code>TransportOutputStream</code>. Called from {@link
     * #send(WebServiceMessage)}.
     * <p/>
     * Default implementation does nothing.
     *
     * @param message the message
     * @throws IOException when an I/O exception occurs
     */
    protected void onSend(WebServiceMessage message) throws IOException {

    }

    /**
     * Returns a <code>TransportInputStream</code>. Called from {@link #receive(WebServiceMessageFactory)}.
     *
     * @return the input stream, or <code>null</code> if no response can be read
     * @throws IOException when an I/O exception occurs
     */
    protected abstract TransportInputStream createTransportInputStream() throws IOException;

    /**
     * Called when the given message has been written to the <code>TransportOutputStream</code>. Called from {@link
     * #receive(WebServiceMessageFactory)}.
     * <p/>
     * Default implementation does nothing.
     *
     * @param message the message
     * @throws IOException when an I/O exception occurs
     */
    protected void onReceive(WebServiceMessage message) throws IOException {

    }

}

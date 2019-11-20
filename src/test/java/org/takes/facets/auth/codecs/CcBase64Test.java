/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.facets.auth.codecs;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Map;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.takes.facets.auth.Identity;

/**
 * Test case for {@link CcBase64}.
 * @since 0.13
 */
public final class CcBase64Test {

    /**
     * CcBase64 can encode.
     * @throws IOException If some problem inside
     */
    @Test
    public void encodes() throws IOException {
        MatcherAssert.assertThat(
            new String(
                new CcBase64(new CcPlain()).encode(
                    new Identity.Simple("urn:test:3")
                )
            ),
            Matchers.equalTo("dXJuJTNBdGVzdCUzQTM=")
        );
    }

    /**
     * CcBase64 can decode.
     * @throws IOException If some problem inside
     */
    @Test
    public void decodes() throws IOException {
        MatcherAssert.assertThat(
            new CcBase64(new CcPlain()).decode(
                "dXJuJTNBdGVzdCUzQXRlc3Q="
                    .getBytes()
            ).urn(),
            Matchers.equalTo("urn:test:test")
        );
    }

    /**
     * CcBase64 can encode and decode.
     * @throws IOException If some problem inside
     */
    @Test
    public void encodesAndDecodes() throws IOException {
        final String urn = "urn:test:Hello World!";
        final Map<String, String> properties =
            ImmutableMap.of("userName", "user");
        final Codec codec = new CcBase64(new CcPlain());
        final Identity expected = codec.decode(
            codec.encode(new Identity.Simple(urn, properties))
        );
        MatcherAssert.assertThat(
            expected.urn(),
            Matchers.equalTo(urn)
        );
        MatcherAssert.assertThat(
            expected.properties(),
            Matchers.equalTo(properties)
        );
    }

    /**
     * CcBase64 can encode empty byte array.
     * @throws IOException If some problem inside
     */
    @Test
    public void encodesEmptyByteArray() throws IOException {
        MatcherAssert.assertThat(
            new String(
                new CcBase64(new CcPlain()).encode(
                    new Identity.Simple("")
                )
            ),
            Matchers.equalTo("")
        );
    }

    /**
     * CcBase64 can decode non Base64 alphabet symbols.
     * @throws IOException If some problem inside
     */
    @Test
    public void decodesNonBaseSixtyFourAlphabetSymbols() throws IOException {
        try {
            new CcStrict(new CcBase64(new CcPlain())).decode(
                " ^^^".getBytes()
            );
        } catch (final DecodingException ex) {
            MatcherAssert.assertThat(
                ex.getMessage(),
                Matchers.equalTo(
                    "Illegal character in Base64 encoded data. [32, 94, 94, 94]"
                )
            );
        }
    }

    /**
     * Checks CcBase64 equals method.
     * @throws Exception If some problem inside
     */
    @Test
    public void equalsTest() throws Exception {
        new Assertion<>(
            "CcBase64 objects must be equal",
            new CcBase64(new CcPlain()),
            new IsEqual<>(
                new CcBase64(new CcPlain())
            )
        ).affirm();
    }

    @Test
    public void hashCodeTest() throws Exception {
        new Assertion<>(
            "Hash codes must be equal",
            new CcBase64(new CcPlain()).hashCode(),
            new IsEqual<>(
                new CcBase64(new CcPlain()).hashCode()
            )
        ).affirm();
    }
}

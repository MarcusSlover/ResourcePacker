/*
 * MIT License
 *
 * Copyright (c) 2022 MarcusSlover
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.marcusslover.resourcepacker;

import com.marcusslover.resourcepacker.api.IResourcePacker;
import com.marcusslover.resourcepacker.core.element.block.RPBlock;
import com.marcusslover.resourcepacker.core.packer.RPPacker;
import com.marcusslover.resourcepacker.core.registry.RPBlockRegistry;
import com.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {

    }

    private static class CustomResourcePack implements IResourcePacker {

        @Override
        public void pack(RPPacker packer) {
            packer.setDescription(Arrays.asList("Hello"));
            packer.setPrefix("Testedd");
            ResourceHelper r = packer.resources();

            /*Blocks*/
            RPBlockRegistry b = packer.blocks();
            b.register(RPBlock.of("Sign Block", r.get("sign.png"))
                    .model(null)
            );
        }
    }
}

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

package com.marcusslover.resourcepacker.util;

import com.marcusslover.resourcepacker.core.element.block.RPState;
import com.marcusslover.resourcepacker.core.resource.ResourcesCache;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BlockUtil {
    public static final Integer NOTE_LIMIT = 24;
    public static final String[] INSTRUMENTS =
            {
                    "harp", "pling", "banjo", "bit",
                    "didgeridoo", "cow_bell", "iron_xylophone", "xylophone",
                    "chime", "guitar", "bell", "flute",
                    "bass", "hat", "snare", "bassdrum"
            };

    private BlockUtil() {
    }

    public static RPState craftBlock(@NotNull int customModelData) {
        return ResourcesCache.integer().get(customModelData, supply(customModelData), RPState.class);
    }

    @NotNull
    private static Supplier<RPState> supply(@NotNull int customModelData) {
        return () -> {
            RPState state = new RPState();
            int cmd = 1;
            int instrument = 0;
            int note = 0;
            int powered = 0;
            boolean pow = false;
            for (int i = 0; i < 736; i++) {
                if (note > BlockUtil.NOTE_LIMIT) {
                    note = 0;
                    instrument++;
                }
                pow = powered == 0;
                powered++;
                if (powered == 2) {
                    note++;
                    powered = 0;
                }
                if (cmd == customModelData) {
                    break;
                }
                cmd++;
            }
            state.add(new RPState.Element("note", String.valueOf(note)));
            state.add(new RPState.Element("instrument", INSTRUMENTS[instrument]));
            state.add(new RPState.Element("powered", String.valueOf(pow)));
            return state;
        };
    }
}

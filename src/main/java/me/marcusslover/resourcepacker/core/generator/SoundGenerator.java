/*
 * MIT License
 *
 * Copyright (c) 2021 MarcusSlover
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

package me.marcusslover.resourcepacker.core.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.marcusslover.resourcepacker.api.IGenerator;
import me.marcusslover.resourcepacker.core.element.sound.RPSound;
import me.marcusslover.resourcepacker.core.registry.RPSoundRegistry;
import me.marcusslover.resourcepacker.util.FileUtil;
import me.marcusslover.resourcepacker.util.JsonUtil;

import java.io.File;
import java.util.List;

public class SoundGenerator implements IGenerator<RPSound, RPSoundRegistry> {

    private final JsonObject log;

    SoundGenerator() {
        log = new JsonObject();
    }

    @Override
    public JsonObject log() {
        return log;
    }

    @Override
    public void generate(File mc, File packer, RPSoundRegistry registry) {
        List<RPSound> list = registry.list();

        File sounds = FileUtil.safeDir(mc, "sounds");
        File packerSounds = FileUtil.safeDir(sounds, "packer");
        File file = FileUtil.safeFile(mc, "sounds.json");
        JsonObject fileJson = new JsonObject();
        for (RPSound sound : list) {
            sound.copyFile(packerSounds);
            JsonObject soundModel = new JsonObject();
            JsonArray soundsArray = new JsonArray();
            JsonObject soundsJson = new JsonObject();
            soundsJson.addProperty("name", "packer/" + sound.name());
            soundsArray.add(soundsJson);
            soundModel.add("sounds", soundsArray);
            fileJson.add("packer." + sound.name(), soundModel);
        }
        JsonUtil.writeFile(file, fileJson);
    }
}

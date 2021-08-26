package me.marcusslover.resourcepacker;

import me.marcusslover.resourcepacker.api.IResourcePacker;
import me.marcusslover.resourcepacker.core.internal.Packer;
import me.marcusslover.resourcepacker.core.internal.RPBlockRegistry;
import me.marcusslover.resourcepacker.core.object.block.RPBlock;
import me.marcusslover.resourcepacker.core.resource.ResourceHelper;

import java.util.List;

public class ResourcePacker implements IResourcePacker {

    @Override
    public void pack(Packer packer) {
        /*Information for the build*/
        packer.setPrefix("marcusslover");
        packer.setName("CorpusHeroes");
        packer.setDescription(List.of("Created by MarcusSlover"));

        /*Resources*/
        ResourceHelper r = packer.getResources();

        /*Blocks*/
        RPBlockRegistry b = packer.blocks();
        b.register(RPBlock.of("Dark Tile", r.get("dark_tile.png")));
    }
}

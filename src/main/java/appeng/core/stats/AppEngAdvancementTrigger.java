/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2017, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import appeng.core.AppEng;

public class AppEngAdvancementTrigger
        implements ICriterionTrigger<AppEngAdvancementTrigger.Instance>, IAdvancementTrigger {
    private final ResourceLocation ID;
    private final Map<PlayerAdvancements, AppEngAdvancementTrigger.Listeners> listeners = new HashMap<>();

    public AppEngAdvancementTrigger(String parString) {
        super();
        this.ID = new ResourceLocation(AppEng.MOD_ID, parString);
    }

    @Override
    public ResourceLocation getId() {
        return this.ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn,
            ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> listener) {
        AppEngAdvancementTrigger.Listeners l = this.listeners.get(playerAdvancementsIn);

        if (l == null) {
            l = new AppEngAdvancementTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, l);
        }

        l.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn,
            ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> listener) {
        AppEngAdvancementTrigger.Listeners l = this.listeners.get(playerAdvancementsIn);

        if (l != null) {
            l.remove(listener);

            if (l.isEmpty()) {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance deserialize(JsonObject p_230307_1_, ConditionArrayParser p_230307_2_) {
        return new AppEngAdvancementTrigger.Instance(this.getId());
    }

    @Override
    public void trigger(ServerPlayerEntity parPlayer) {
        AppEngAdvancementTrigger.Listeners l = this.listeners.get(parPlayer.getAdvancements());

        if (l != null) {
            l.trigger(parPlayer);
        }
    }

    public static class Instance implements ICriterionInstance {
        private final ResourceLocation id;

        public Instance(ResourceLocation id) {
            this.id = id;
        }

        public boolean test() {
            return true;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public JsonObject serialize(ConditionArraySerializer p_230240_1_) {
            return new JsonObject();
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance>> listeners = new HashSet<>();

        Listeners(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(PlayerEntity player) {
            List<ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> listener : this.listeners) {
                if (listener.getCriterionInstance().test()) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener<AppEngAdvancementTrigger.Instance> l : list) {
                    l.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}

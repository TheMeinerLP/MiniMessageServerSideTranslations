package dev.themeinerlp.mmsst;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

public final class LynxWrapper implements Translator {
    
    private final TranslationRegistry wrappedRegistry;

    public LynxWrapper(TranslationRegistry wrappedRegistry) {
        this.wrappedRegistry = wrappedRegistry;
    }

    @Override
    public @NotNull Key name() {
        return this.wrappedRegistry.name();
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return this.wrappedRegistry.translate(key, locale);
    }

    @Override
    public @Nullable Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        final var miniMessageResult = this.translate(component.key(), locale);
        if (miniMessageResult == null) return null;
        final var values = new String[component.args().size()];
        for (int i = 0; i < component.args().size(); i++) {
            values[i] = MiniMessage.miniMessage().serialize(component.args().get(i));
        }
        final var resultComponent = MiniMessage.miniMessage().deserialize(miniMessageResult.format(values));
        final var children = resultComponent.children();
        children.replaceAll(child -> GlobalTranslator.render(child, locale));
        return GlobalTranslator.render(resultComponent, locale).children(children);
    }
}

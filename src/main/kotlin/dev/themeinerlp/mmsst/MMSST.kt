package dev.themeinerlp.mmsst

import cloud.commandframework.annotations.AnnotationParser
import cloud.commandframework.arguments.parser.ParserParameters
import cloud.commandframework.arguments.parser.StandardParameters
import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.CommandMeta
import cloud.commandframework.minecraft.extras.MinecraftHelp
import cloud.commandframework.paper.PaperCommandManager
import dev.themeinerlp.mmsst.commands.FlightCommand
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.util.UTF8ResourceBundleControl
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Level
import java.util.function.Function
class MMSST : JavaPlugin() {
    private val supportedLocals: Array<Locale> = arrayOf(Locale.US, Locale.GERMAN)

    private lateinit var paperCommandManager: PaperCommandManager<CommandSender>
    private lateinit var annotationParser: AnnotationParser<CommandSender>

    override fun onEnable() {

        val registry = TranslationRegistry.create(Key.key("mmsst", "localization"))
        supportedLocals.forEach { locale ->
            val bundle = ResourceBundle.getBundle("mmsst", locale, UTF8ResourceBundleControl.get())
            registry.registerAll(locale, bundle, false)
        }
        registry.defaultLocale(supportedLocals.first())
        GlobalTranslator.translator().addSource(registry)

        buildCommandSystem()
        registerCommands()
    }

    /**
     * Register some commands from this plugin
     */
    private fun registerCommands() {
        annotationParser.parse(FlightCommand(this))
    }

    /**
     * Create the command system
     */
    private fun buildCommandSystem() {
        try {
            paperCommandManager = PaperCommandManager(
                this,
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
            )
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Failed to build command system", e)
            server.pluginManager.disablePlugin(this)
            return
        }

        if (paperCommandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            paperCommandManager.registerBrigadier()
            logger.info("Brigadier support enabled")
        }

        if (paperCommandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            paperCommandManager.registerAsynchronousCompletions()
            logger.info("Asynchronous completions enabled")
        }


        val commandMetaFunction =
            Function<ParserParameters, CommandMeta> { p: ParserParameters ->
                CommandMeta.simple().with(
                    CommandMeta.DESCRIPTION,
                    p.get(StandardParameters.DESCRIPTION, "No description")
                ).build()
            }

        annotationParser = AnnotationParser(
            paperCommandManager,
            CommandSender::class.java, commandMetaFunction
        )
    }

    fun getPluginPrefix(): String {
        return "<lang:plugin.prefix:${this.name}>"
    }


}
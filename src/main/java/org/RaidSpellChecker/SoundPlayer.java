package org.RaidSpellChecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import net.runelite.client.util.Filepath;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SoundPlayer
{

    private final AudioPlayer audioPlayer;
    private final RaidSpellCheckerConfig config;

    private Filepath customDir;

    public void initialize(Filepath pluginDirectory)
    {
        customDir = pluginDirectory;
    }

    public void play(String fileName, int volume)
    {
        try
        {
            float gainDb = 20f * (float) Math.log10(config.soundVolume() / 100f);

            // 1) Try custom file on disk first
            Filepath customPath = customDir.joinSegment(fileName);
            if (customPath.exists())
            {
                audioPlayer.play(customPath, gainDb);
                return;
            }

            // 2) Fallback to bundled resource in src/main/resources/sounds/
            audioPlayer.play(this.getClass(), "/sounds/" + fileName, gainDb);
        }
        catch (Exception e)
        {
            log.error("Failed to play sound '{}'", fileName, e);
        }
    }
}
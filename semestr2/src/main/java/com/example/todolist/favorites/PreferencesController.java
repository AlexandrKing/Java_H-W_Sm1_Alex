package com.example.todolist.favorites;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * REST Controller for managing user preferences and favorites.
 * Provides endpoints for managing user preferences via session/cookies.
 */
@RestController
@RequestMapping("/api/preferences")
@Tag(name = "User Preferences", description = "Operations for managing user preferences and favorites")
public class PreferencesController {

    private final UserPreferencesService preferencesService;

    @Autowired
    public PreferencesController(UserPreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    /**
     * Add a task to favorites.
     * @param taskId the task ID
     * @param session the HTTP session
     * @return success response
     */
    @PostMapping("/favorites/{taskId}")
    @Operation(summary = "Add to favorites", description = "Add a task to user's favorites")
    public ResponseEntity<Map<String, String>> addToFavorites(
            @PathVariable Long taskId,
            HttpSession session) {
        preferencesService.addToFavorites(taskId, session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task " + taskId + " added to favorites");
        return ResponseEntity.ok(response);
    }

    /**
     * Remove a task from favorites.
     * @param taskId the task ID
     * @param session the HTTP session
     * @return success response
     */
    @DeleteMapping("/favorites/{taskId}")
    @Operation(summary = "Remove from favorites", description = "Remove a task from user's favorites")
    public ResponseEntity<Map<String, String>> removeFromFavorites(
            @PathVariable Long taskId,
            HttpSession session) {
        preferencesService.removeFromFavorites(taskId, session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task " + taskId + " removed from favorites");
        return ResponseEntity.ok(response);
    }

    /**
     * Get all favorite task IDs.
     * @param session the HTTP session
     * @return set of favorite task IDs
     */
    @GetMapping("/favorites")
    @Operation(summary = "Get favorites", description = "Get all favorite task IDs for the user")
    public ResponseEntity<Map<String, Object>> getFavorites(HttpSession session) {
        Set<Long> favorites = preferencesService.getFavorites(session);
        Map<String, Object> response = new HashMap<>();
        response.put("favorites", favorites);
        response.put("count", favorites.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Check if a task is favorite.
     * @param taskId the task ID
     * @param session the HTTP session
     * @return true/false response
     */
    @GetMapping("/favorites/{taskId}")
    @Operation(summary = "Check favorite status", description = "Check if a task is in user's favorites")
    public ResponseEntity<Map<String, Object>> isFavorite(
            @PathVariable Long taskId,
            HttpSession session) {
        boolean isFavorite = preferencesService.isFavorite(taskId, session);
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("isFavorite", isFavorite);
        return ResponseEntity.ok(response);
    }

    /**
     * Set sort preference.
     * @param preference the sort preference
     * @param session the HTTP session
     * @return success response
     */
    @PostMapping("/sort-preference")
    @Operation(summary = "Set sort preference", description = "Set how tasks should be sorted")
    public ResponseEntity<Map<String, String>> setSortPreference(
            @RequestParam String preference,
            HttpSession session) {
        preferencesService.setSortPreference(preference, session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sort preference set to: " + preference);
        return ResponseEntity.ok(response);
    }

    /**
     * Get sort preference.
     * @param session the HTTP session
     * @return current sort preference
     */
    @GetMapping("/sort-preference")
    @Operation(summary = "Get sort preference", description = "Get current sort preference for user")
    public ResponseEntity<Map<String, String>> getSortPreference(HttpSession session) {
        String preference = preferencesService.getSortPreference(session);
        Map<String, String> response = new HashMap<>();
        response.put("sortPreference", preference);
        return ResponseEntity.ok(response);
    }

    /**
     * Set theme preference.
     * @param theme the theme (light/dark)
     * @param session the HTTP session
     * @return success response
     */
    @PostMapping("/theme")
    @Operation(summary = "Set theme preference", description = "Set UI theme preference (light/dark)")
    public ResponseEntity<Map<String, String>> setThemePreference(
            @RequestParam String theme,
            HttpSession session) {
        preferencesService.setThemePreference(theme, session);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Theme set to: " + theme);
        return ResponseEntity.ok(response);
    }

    /**
     * Get theme preference.
     * @param session the HTTP session
     * @return current theme preference
     */
    @GetMapping("/theme")
    @Operation(summary = "Get theme preference", description = "Get current UI theme preference for user")
    public ResponseEntity<Map<String, String>> getThemePreference(HttpSession session) {
        String theme = preferencesService.getThemePreference(session);
        Map<String, String> response = new HashMap<>();
        response.put("theme", theme);
        return ResponseEntity.ok(response);
    }
}

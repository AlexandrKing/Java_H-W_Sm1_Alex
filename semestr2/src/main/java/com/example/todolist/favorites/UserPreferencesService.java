package com.example.todolist.favorites;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Service for managing user preferences and favorites.
 * Stores data in HTTP session.
 */
@Service
public class UserPreferencesService {

    private static final String FAVORITES_SESSION_KEY = "favoriteTaskIds";
    private static final String SORT_PREFERENCE_KEY = "sortPreference";
    private static final String THEME_PREFERENCE_KEY = "themePreference";

    /**
     * Add a task to favorites.
     * @param taskId the task ID to add
     * @param session the HTTP session
     */
    public void addToFavorites(Long taskId, HttpSession session) {
        Set<Long> favorites = getFavorites(session);
        favorites.add(taskId);
        session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }

    /**
     * Remove a task from favorites.
     * @param taskId the task ID to remove
     * @param session the HTTP session
     */
    public void removeFromFavorites(Long taskId, HttpSession session) {
        Set<Long> favorites = getFavorites(session);
        favorites.remove(taskId);
        session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }

    /**
     * Get all favorite task IDs.
     * @param session the HTTP session
     * @return set of favorite task IDs
     */
    @SuppressWarnings("unchecked")
    public Set<Long> getFavorites(HttpSession session) {
        Set<Long> favorites = (Set<Long>) session.getAttribute(FAVORITES_SESSION_KEY);
        if (favorites == null) {
            favorites = new HashSet<>();
            session.setAttribute(FAVORITES_SESSION_KEY, favorites);
        }
        return favorites;
    }

    /**
     * Check if a task is favorite.
     * @param taskId the task ID
     * @param session the HTTP session
     * @return true if task is favorite, false otherwise
     */
    public boolean isFavorite(Long taskId, HttpSession session) {
        return getFavorites(session).contains(taskId);
    }

    /**
     * Set sort preference for user.
     * @param preference the sort preference (e.g., "createdAt", "dueDate", "priority")
     * @param session the HTTP session
     */
    public void setSortPreference(String preference, HttpSession session) {
        session.setAttribute(SORT_PREFERENCE_KEY, preference);
    }

    /**
     * Get sort preference for user.
     * @param session the HTTP session
     * @return the sort preference, default "createdAt"
     */
    public String getSortPreference(HttpSession session) {
        String preference = (String) session.getAttribute(SORT_PREFERENCE_KEY);
        return preference != null ? preference : "createdAt";
    }

    /**
     * Set theme preference for user.
     * @param theme the theme preference (e.g., "light", "dark")
     * @param session the HTTP session
     */
    public void setThemePreference(String theme, HttpSession session) {
        session.setAttribute(THEME_PREFERENCE_KEY, theme);
    }

    /**
     * Get theme preference for user.
     * @param session the HTTP session
     * @return the theme preference, default "light"
     */
    public String getThemePreference(HttpSession session) {
        String theme = (String) session.getAttribute(THEME_PREFERENCE_KEY);
        return theme != null ? theme : "light";
    }
}

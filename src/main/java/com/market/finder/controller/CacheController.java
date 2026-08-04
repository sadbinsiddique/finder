package com.market.finder.controller;

import com.market.finder.service.CacheManagementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/cache")
public class CacheController {

    private final CacheManagementService cacheManagementService;

    public CacheController(CacheManagementService cacheManagementService) {
        this.cacheManagementService = cacheManagementService;
    }

    @GetMapping
    public String viewCacheDashboard(Model model) {
        List<CacheManagementService.CacheDetailDTO> overview = cacheManagementService.getCacheOverview();
        model.addAttribute("caches", overview);
        model.addAttribute("totalCaches", overview.size());
        model.addAttribute("totalKeys", cacheManagementService.getTotalCacheKeys());
        model.addAttribute("currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return "admin/cache/index";
    }

    @PostMapping("/flush-all")
    public String flushAllCaches(RedirectAttributes redirectAttributes) {
        cacheManagementService.flushAllCaches();
        redirectAttributes.addFlashAttribute("successMessage", "All application caches have been flushed successfully.");
        return "redirect:/admin/cache";
    }

    @PostMapping("/flush")
    public String flushSpecificCache(@RequestParam String cacheName, RedirectAttributes redirectAttributes) {
        boolean result = cacheManagementService.flushCache(cacheName);
        if (result) {
            redirectAttributes.addFlashAttribute("successMessage", "Cache '" + cacheName + "' was flushed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to flush cache '" + cacheName + "'. Cache not found.");
        }
        return "redirect:/admin/cache";
    }

    @PostMapping("/evict-key")
    public String evictSingleKey(@RequestParam String cacheName,
                                 @RequestParam String key,
                                 RedirectAttributes redirectAttributes) {
        if (key == null || key.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cache key cannot be empty.");
            return "redirect:/admin/cache";
        }
        boolean evicted = cacheManagementService.evictKey(cacheName, key);
        if (evicted) {
            redirectAttributes.addFlashAttribute("successMessage", "Key '" + key + "' evicted from cache '" + cacheName + "'.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not evict key '" + key + "' from cache '" + cacheName + "'.");
        }
        return "redirect:/admin/cache";
    }

    @PostMapping("/warmup")
    public String warmupCaches(RedirectAttributes redirectAttributes) {
        cacheManagementService.warmupCaches();
        redirectAttributes.addFlashAttribute("successMessage", "Caches re-warmed and preloaded successfully from database.");
        return "redirect:/admin/cache";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCacheData() {
        String jsonStr = cacheManagementService.exportCacheDataAsJson();
        byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String filename = "finder-cache-backup-" + timestamp + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(bytes);
    }

    @PostMapping("/import")
    public String importCacheData(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam(value = "jsonText", required = false) String jsonText,
                                  RedirectAttributes redirectAttributes) {
        try {
            String content = null;
            if (file != null && !file.isEmpty()) {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            } else if (jsonText != null && !jsonText.isBlank()) {
                content = jsonText;
            }

            if (content == null || content.isBlank()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select a backup file or paste JSON data to import.");
                return "redirect:/admin/cache";
            }

            boolean imported = cacheManagementService.importCacheDataFromJson(content);
            if (imported) {
                redirectAttributes.addFlashAttribute("successMessage", "Cache data imported and restored into memory successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to import cache data. Invalid JSON payload or missing structure.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing import file: " + e.getMessage());
        }
        return "redirect:/admin/cache";
    }

    @GetMapping("/api/overview")
    @ResponseBody
    public List<CacheManagementService.CacheDetailDTO> getCacheOverviewApi() {
        return cacheManagementService.getCacheOverview();
    }
}

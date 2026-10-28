<script setup lang="ts">
import http from "../http-common";
import { ref, onMounted, computed, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import AppHeader from './AppHeader.vue';
//import { useAuth } from '../stores/auth';

//const { api } = useAuth();

const router = useRouter();

const API_BASE_URL = http.defaults.baseURL;

const allocationSnapshots = ref<any[]>([]);
const isLoading = ref(true);
const error = ref('');
const searchQuery = ref('');
const currentPage = ref(0);
const totalPages = ref(0);
const totalElements = ref(0);
const pageSize = ref(20);
const searchInputRef = ref<HTMLInputElement | null>(null);
const viewMode = ref<'cards' | 'table'>('table');
const sortBy = ref('allocationSnapshotId');
const sortOrder = ref('desc');

const shouldMaintainFocus = ref(false);

const filteredAllocationSnapshots = computed(() => {
  return allocationSnapshots.value;
});

const visiblePages = computed(() => {
  const maxVisible = 5;
  const half = Math.floor(maxVisible / 2);
  
  let start = Math.max(0, currentPage.value - half);
  let end = Math.min(totalPages.value - 1, start + maxVisible - 1);
  
  // Adjust start if we're near the end
  if (end - start + 1 < maxVisible) {
    start = Math.max(0, end - maxVisible + 1);
  }
  
  const pages = [];
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  
  return pages;
});

const loadAllocationSnapshots = async (page = 0) => {
  const wasSearchFocused = document.activeElement === searchInputRef.value;
  
  isLoading.value = true;
  error.value = '';

  try {
    const response = await fetch(`${API_BASE_URL}/allocationSnapshot/allocationSnapshots?sortBy=${sortBy.value}&sortOrder=${sortOrder.value}&searchQuery=${searchQuery.value}&page=${page}&size=${pageSize.value}`);
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const data = await response.json();
    
    allocationSnapshots.value = data.allocationSnapshots || [];
    totalElements.value = data.totalElements || 0;
    totalPages.value = Math.ceil(totalElements.value / pageSize.value);
    currentPage.value = page;
  } catch (err: any) {
    console.error('Get allocationSnapshots error:', err);
    error.value = err.message || 'Failed to load allocationSnapshots';
  }

  isLoading.value = false;
  
  // Restore focus if it was on search input before API call
  if (wasSearchFocused && searchInputRef.value) {
    nextTick(() => {
      searchInputRef.value?.focus();
    });
  }
};

const handleSearch = () => {
  shouldMaintainFocus.value = true;
  loadAllocationSnapshots(0);
};

const handleSearchBlur = (event: FocusEvent) => {
  // Prevent blur if we should maintain focus or clicking within search container
  const relatedTarget = event.relatedTarget as HTMLElement;
  if (shouldMaintainFocus.value || (relatedTarget && searchInputRef.value?.contains(relatedTarget))) {
    event.preventDefault();
    searchInputRef.value?.focus();
    return;
  }
  shouldMaintainFocus.value = false;
};

const handleSearchKeydown = (event: KeyboardEvent) => {
  // Allow Escape key to blur the search input
  if (event.key === 'Escape') {
    shouldMaintainFocus.value = false;
    searchInputRef.value?.blur();
  }
};

const handleSort = (column: string) => {
  if (sortBy.value === column) {
    // Toggle sort order if same column
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    // Set new column and default to ascending
    sortBy.value = column;
    sortOrder.value = 'asc';
  }
  loadAllocationSnapshots(0);
};

const getSortIcon = (column: string) => {
  if (sortBy.value !== column) return '↕️';
  return sortOrder.value === 'asc' ? '↑' : '↓';
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    loadAllocationSnapshots(currentPage.value + 1);
  }
};

const prevPage = () => {
  if (currentPage.value > 0) {
    loadAllocationSnapshots(currentPage.value - 1);
  }
};

const goToPage = (page: number) => {
  if (page >= 0 && page < totalPages.value) {
    loadAllocationSnapshots(page);
  }
};

const getAllocationSnapshotIcon = (allocationSnapshot: any) => {
  const icons = ['👤', '👨', '👩', '🧑', '👱', '🧔', '👨‍💼', '👩‍💼', '🧑‍💻', '👨‍🔬'];
  return icons[allocationSnapshot.allocationSnapshotId % icons.length];
};

const viewAllocationSnapshotDetails = (allocationSnapshot: any) => {
  router.push({ name: 'AllocationSnapshotDetail', params: { id: allocationSnapshot.allocationSnapshotId } });
};

const toggleView = () => {
  viewMode.value = viewMode.value === 'cards' ? 'table' : 'cards';
};

const isDarkMode = ref(true);

const lightPalette = {
  '--color-primary': '#2196f3',
  '--color-primary-dark': '#1976d2',
  '--color-bg-dark': '#f0f8ff',
  '--color-bg-medium': '#e3f2fd',
  '--color-bg-light': '#bbdefb',
  '--color-text-primary': '#0d47a1',
  '--color-text-secondary': 'rgba(13, 71, 161, 0.8)',
  '--color-text-muted': 'rgba(13, 71, 161, 0.7)',
  '--color-text-placeholder': 'rgba(13, 71, 161, 0.5)',
  '--color-border-light': 'rgba(13, 71, 161, 0.1)',
  '--color-border-medium': 'rgba(13, 71, 161, 0.2)',
  '--color-border-dark': 'rgba(13, 71, 161, 0.3)',
  '--color-success': '#4caf50',
  '--color-warning': '#ff9800',
  '--color-error': '#f44336',
  '--color-muted': '#607d8b'
};

const darkPalette = {
  '--color-primary': '#3498db',
  '--color-primary-dark': '#2980b9',
  '--color-bg-dark': '#282c34',
  '--color-bg-medium': '#353b48',
  '--color-bg-light': '#3e4451',
  '--color-text-primary': '#d3d7de',
  '--color-text-secondary': 'rgba(211, 215, 222, 0.8)',
  '--color-text-muted': 'rgba(211, 215, 222, 0.7)',
  '--color-text-placeholder': 'rgba(211, 215, 222, 0.5)',
  '--color-border-light': 'rgba(211, 215, 222, 0.1)',
  '--color-border-medium': 'rgba(211, 215, 222, 0.2)',
  '--color-border-dark': 'rgba(211, 215, 222, 0.3)',
  '--color-success': '#27ae60',
  '--color-warning': '#f39c12',
  '--color-error': '#e74c3c',
  '--color-muted': '#95a5a6'
};

const applyPalette = (palette: Record<string, string>) => {
  Object.entries(palette).forEach(([property, value]) => {
    document.documentElement.style.setProperty(property, value);
  });
};

const loadThemeState = () => {
  const savedDarkMode = localStorage.getItem('app-is-dark-mode');
  if (savedDarkMode !== null) {
    isDarkMode.value = savedDarkMode === 'true';
  }
};

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value;
  localStorage.setItem('app-is-dark-mode', isDarkMode.value.toString());
  
  if (isDarkMode.value) {
    applyPalette(darkPalette);
  } else {
    applyPalette(lightPalette);
  }
};

onMounted(() => {
  loadThemeState();
  loadAllocationSnapshots();
});
</script>

<template>
  <div class="allocationSnapshots-page">
    <AppHeader />
    
    <header class="allocationSnapshots-header">
      <div class="header-row">
        <div class="header-content">
          <div class="header-info">
            <div class="title-row">
              <h1 class="page-title">AllocationSnapshots</h1>
              <div class="allocationSnapshots-count-inline" v-if="!isLoading">
                ({{ totalElements }})
              </div>
            </div>
          </div>
        </div>

        <div class="search-box-header">
          <input
            ref="searchInputRef"
            v-model="searchQuery"
            @input="handleSearch"
            @blur="handleSearchBlur"
            @keydown="handleSearchKeydown"
            type="text"
            placeholder="Search allocationSnapshots..."
            class="search-input"
          />
        </div>

        <div class="controls-container">
          <button @click="toggleDarkMode" class="dark-mode-toggle-button" :title="isDarkMode ? 'Light Mode' : 'Dark Mode'">
            {{ isDarkMode ? '🌙' : '☀️' }}
          </button>
          <button @click="toggleView" class="toggle-view-button" :title="viewMode === 'cards' ? 'Table View' : 'Card View'">
            {{ viewMode === 'cards' ? '📋' : '🔲' }}
          </button>
          <div v-if="totalPages > 1" class="pagination">
            <div class="pagination-info">
              <button 
                @click="prevPage" 
                :disabled="currentPage === 0"
                class="pagination-button"
              >
                <
              </button>
              
              <span class="page-numbers">
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="goToPage(page)"
                  :class="['page-number', { active: currentPage === page }]"
                >
                  {{ page + 1 }}
                </button>
              </span>
              <button 
                @click="nextPage" 
                :disabled="currentPage >= totalPages - 1"
                class="pagination-button"
              >
                >
              </button>
              <div class="page-size-selector">
                <select 
                  v-model="pageSize" 
                  @change="loadAllocationSnapshots(0)"
                  class="page-size-select"
                >
                  <option value="10">10 / page</option>
                  <option value="20">20 / page</option>
                  <option value="50">50 / page</option>
                  <option value="100">100 / page</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="allocationSnapshots-content">
      <!-- Loading State -->
      <div v-if="isLoading" class="loading-state">
        <div class="loading-spinner-large"></div>
        <p class="loading-text">Loading allocationSnapshots...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="error-state">
        <div class="error-icon">⚠️</div>
        <h3 class="error-title">Failed to Load AllocationSnapshots</h3>
        <p class="error-message">{{ error }}</p>
        <button @click="loadAllocationSnapshots()" class="retry-button">
          Try Again
        </button>
      </div>

      <!-- Empty State -->
      <div v-else-if="filteredAllocationSnapshots.length === 0" class="empty-state">
        <div class="empty-icon">👥</div>
        <h3 class="empty-title">No AllocationSnapshots Found</h3>
        <p class="empty-message">
          {{ searchQuery ? 'No allocationSnapshots match your search criteria.' : 'No allocationSnapshots found in the system.' }}
        </p>
      </div>

      <!-- AllocationSnapshots Grid -->
      <div v-else-if="viewMode === 'cards'" class="allocationSnapshots-grid">
        <div
          v-for="allocationSnapshot in filteredAllocationSnapshots"
          :key="allocationSnapshot.allocationSnapshotId"
          class="allocationSnapshot-card"
          @click="viewAllocationSnapshotDetails(allocationSnapshot)"
        >
          <div class="allocationSnapshot-header">
            <div class="allocationSnapshot-icon">
              {{ getAllocationSnapshotIcon(allocationSnapshot) }}
            </div>
          </div>

          <div class="allocationSnapshot-info">
            <h3 class="allocationSnapshot-name">{{ allocationSnapshot.name || 'Unknown AllocationSnapshot' }}</h3>
            <p class="allocationSnapshot-description">
              AllocationSnapshot ID: {{ allocationSnapshot.allocationSnapshotId }}
            </p>
            
            <div class="allocationSnapshot-metadata">

              <div class="metadata-item">
                <span class="metadata-label">AllocationSnapshotId:</span>
                <span class="metadata-value">{{ allocationSnapshot.allocationSnapshotId || 'N/A' }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-label">AllocationId:</span>
                <span class="metadata-value">{{ allocationSnapshot.allocationId || 'N/A' }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-label">TargetReference:</span>
                <span class="metadata-value">{{ allocationSnapshot.targetReference || 'N/A' }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-label">ActualPercent:</span>
                <span class="metadata-value">{{ allocationSnapshot.actualPercent || 'N/A' }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-label">CapturedAt:</span>
                <span class="metadata-value">{{ allocationSnapshot.capturedAt || 'N/A' }}</span>
              </div>

            </div>
          </div>
        </div>
      </div>

      <!-- AllocationSnapshots Table -->
      <div v-else-if="viewMode === 'table'" class="allocationSnapshots-table-container">
        <table class="allocationSnapshots-table">
          <thead>
            <tr>

              <th @click="handleSort('allocationSnapshotId')" class="sortable-header">
                AllocationSnapshotId {{ getSortIcon('allocationSnapshotId') }}
              </th>
              <th @click="handleSort('allocationId')" class="sortable-header">
                AllocationId {{ getSortIcon('allocationId') }}
              </th>
              <th @click="handleSort('targetReference')" class="sortable-header">
                TargetReference {{ getSortIcon('targetReference') }}
              </th>
              <th @click="handleSort('actualPercent')" class="sortable-header">
                ActualPercent {{ getSortIcon('actualPercent') }}
              </th>
              <th @click="handleSort('capturedAt')" class="sortable-header">
                CapturedAt {{ getSortIcon('capturedAt') }}
              </th>

            </tr>
          </thead>
          <tbody>
            <tr v-for="allocationSnapshot in filteredAllocationSnapshots" :key="allocationSnapshot.allocationSnapshotId" class="allocationSnapshot-row">
              
              
                      <td class="allocationSnapshot-id-cell" @click="viewAllocationSnapshotDetails(allocationSnapshot)">{{ allocationSnapshot.allocationSnapshotId }}</td>
                              <td class="$field.name.camel-cell">
                <div class="$field.name.camel-info-table">
                  <span class="$field.name.camel-name-table">{{ allocationSnapshot.allocationId }}</span>
                </div>
              </td>
                              <td class="$field.name.camel-cell">
                <div class="$field.name.camel-info-table">
                  <span class="$field.name.camel-name-table">{{ allocationSnapshot.targetReference }}</span>
                </div>
              </td>
                              <td class="$field.name.camel-cell">
                <div class="$field.name.camel-info-table">
                  <span class="$field.name.camel-name-table">{{ allocationSnapshot.actualPercent }}</span>
                </div>
              </td>
                              <td class="$field.name.camel-cell">
                <div class="$field.name.camel-info-table">
                  <span class="$field.name.camel-name-table">{{ allocationSnapshot.capturedAt }}</span>
                </div>
              </td>
        
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>

<style scoped>
.allocationSnapshots-page {
  min-height: 100vh;
  background: var(--color-bg-dark);
  color: var(--color-text-primary);
}

.allocationSnapshots-header {
  padding: 20px;
}

.header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1500px;
  margin: 0 auto;
  gap: 20px;
}

.header-content {
  flex: 1;
  min-width: 0;
}

.header-info {
  width: 100%;
}

.page-title {
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0;
  margin-top: 12px;
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-row {
  display: flex;
  align-items: baseline;
  gap: 16px;
}

.allocationSnapshots-count-inline {
  color: var(--color-text-muted);
  font-size: 0.9rem;
  font-weight: 400;
  white-space: nowrap;
}

.search-box-header {
  flex: 0 0 300px;
  display: flex;
  justify-content: center;
}

.search-box-header .search-input {
  width: 100%;
  padding: 12px 16px;
  background: var(--color-bg-light);
  border: 1px solid var(--color-border-medium);
  border-radius: 8px;
  color: var(--color-text-primary);
  font-size: 1rem;
  transition: all 0.2s ease;
}

.search-box-header .search-input:focus {
  outline: none;
  border-color: var(--color-primary);
  background: var(--color-bg-light);
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.search-box-header .search-input::placeholder {
  color: var(--color-text-placeholder);
}

.controls-container {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  min-width: 0;
}

.allocationSnapshots-content {
  max-width: 2000px;
  margin: 0 auto;
  padding: 15px 20px;
}

.loading-state,
.error-state,
.empty-state {
  text-align: center;
  padding: 80px 24px;
}

.loading-spinner-large {
  width: 48px;
  height: 48px;
  border: 4px solid var(--color-border-medium);
  border-top: 4px solid var(--color-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 24px;
}

.loading-text {
  color: var(--color-text-secondary);
  font-size: 1.1rem;
  margin: 0;
}

.error-icon,
.empty-icon {
  font-size: 4rem;
  margin-bottom: 24px;
}

.error-title,
.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: var(--color-text-primary);
}

.error-message,
.empty-message {
  color: var(--color-text-secondary);
  margin: 0 0 32px 0;
  font-size: 1rem;
}

.retry-button {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
  border: none;
  color: var(--color-text-primary);
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.retry-button:hover {
  background: linear-gradient(135deg, var(--color-primary-dark), var(--color-primary));
  transform: translateY(-1px);
}

.allocationSnapshots-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(252px, 1fr));
  gap: 24px;
}

.allocationSnapshot-card {
  background: var(--color-bg-medium);
  border: 1px solid var(--color-border-light);
  border-radius: 16px;
  padding: 24px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.allocationSnapshot-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.3);
  border-color: var(--color-primary);
}

.allocationSnapshot-header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
}

.allocationSnapshot-icon {
  font-size: 2.5rem;
}

.allocationSnapshot-info {
  margin-bottom: 20px;
}

.allocationSnapshot-name {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--color-text-primary);
}

.allocationSnapshot-description {
  color: var(--color-text-secondary);
  margin: 0 0 16px 0;
  line-height: 1.5;
  font-size: 0.9rem;
}

.allocationSnapshot-metadata {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.metadata-item {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
}

.metadata-label {
  color: var(--color-text-muted);
}

.metadata-value {
  color: var(--color-text-secondary);
  font-family: monospace;
}

/* Pagination */
.pagination-button {
  background: transparent;
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-primary);
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9rem;
}

.pagination-button:hover:not(:disabled) {
  border-color: var(--color-text-primary);
  transform: translateY(-1px);
}

.pagination-button:disabled {
  border-color: var(--color-bg-light);
  color: var(--color-text-placeholder);
  cursor: not-allowed;
  transform: none;
}

.pagination-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-numbers {
  display: flex;
  gap: 4px;
}

.page-number {
  background: transparent;
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-primary);
  padding: 6px 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.8rem;
}

.page-number:hover {
  border-color: var(--color-primary);
  background: rgba(52, 152, 219, 0.1);
  color: var(--color-primary);
}

.page-number.active {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-dark));
  border-color: var(--color-primary);
  color: var(--color-text-primary);
}

.toggle-view-button {
  background: var(--color-bg-light);
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-primary);
  padding: 12px 16px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-right: 16px;
}

.toggle-view-button:hover {
  border-color: var(--color-primary);
  background: rgba(52, 152, 219, 0.1);
  color: var(--color-primary);
}

.dark-mode-toggle-button {
  background: var(--color-bg-light);
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-primary);
  padding: 12px 16px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-right: 16px;
}

.dark-mode-toggle-button:hover {
  border-color: var(--color-primary);
  background: rgba(52, 152, 219, 0.1);
  color: var(--color-primary);
}

/* Table Styles */
.allocationSnapshots-table-container {
  background: var(--color-bg-medium);
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid var(--color-border-light);
}

.allocationSnapshots-table {
  width: 100%;
  border-collapse: collapse;
  border-spacing: 0;
}

.allocationSnapshots-table th {
  background: var(--color-bg-light);
  color: var(--color-text-primary);
  padding: 16px 20px;
  text-align: center;
  font-weight: 600;
  font-size: 0.9rem;
  border-bottom: 1px solid var(--color-border-light);
}

.allocationSnapshots-table th:first-child,
.allocationSnapshots-table td:first-child {
  width: 140px;
  min-width: 140px;
  max-width: 140px;
}

.sortable-header {
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s ease;
}

.sortable-header:hover {
  background: rgba(52, 152, 219, 0.1);
}

.allocationSnapshot-row {
  transition: background-color 0.2s ease;
}

.allocationSnapshot-row:hover {
  background: var(--color-bg-light);
}

.allocationSnapshots-table td {
  padding: 4px 4px;
  border-bottom: 1px solid var(--color-border-light);
  color: var(--color-text-primary);
  font-size: 0.9rem;
  vertical-align: middle;
  text-align: center;
}

.allocationSnapshot-info-table {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.allocationSnapshot-icon-small {
  font-size: 1.5rem;
}

.allocationSnapshot-name-table {
  font-weight: 600;
  color: var(--color-text-primary);
}

.allocationSnapshot-id-cell {
  font-family: monospace;
  color: var(--color-primary) !important;
  cursor: pointer;
  font-weight: 500;
}

.allocationSnapshot-id-cell:hover {
  color: var(--color-primary-dark);
}

.page-size-selector {
  display: flex;
  align-items: center;
  margin-left: 16px;
  font-size: 0.9rem;
}

.page-size-select {
  background: var(--color-bg-light);
  border: 1px solid var(--color-border-dark);
  color: var(--color-text-primary);
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.page-size-select:focus {
  outline: none;
  border-color: var(--color-primary);
  background: var(--color-bg-light);
}

.page-size-select:hover {
  border-color: var(--color-text-secondary);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .allocationSnapshots-grid {
    grid-template-columns: 1fr;
  }
  
  .page-title {
    font-size: 1.75rem;
  }
  
  .pagination-info {
    order: -1;
  }
  
  .page-numbers {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>

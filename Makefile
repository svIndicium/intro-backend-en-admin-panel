# Makefile voor data directory cleanup

# Default data directory (pas aan indien nodig)
DATA_DIR = ./data

# Help command - laat alle beschikbare targets zien
.PHONY: help
help:
	@echo "Available commands:"
	@echo "  make clean   - Force clear without confirmation"


# Force clear without confirmation (gebruik met voorzichtigheid!)
.PHONY: clean
clean:
	@echo "🔥 Force clearing data directory..."
	@rm -f $(DATA_DIR)/*
	@echo "✅ Data directory cleared"


# Show data directory size
.PHONY: info
info:
	@echo "Data directory: $(DATA_DIR)"
	@if [ -d "$(DATA_DIR)" ]; then \
		echo "Size: $$(du -sh $(DATA_DIR) | cut -f1)"; \
		echo "Files: $$(find $(DATA_DIR) -type f | wc -l)"; \
	else \
		echo "Directory does not exist"; \
	fi